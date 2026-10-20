package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.DealerEnquiryResponse;
import com.myvehiclechoice.dto.VehicleDetailDto;
import com.myvehiclechoice.dto.admin.AdminStatsDto;
import com.myvehiclechoice.exception.ResourceNotFoundException;
import com.myvehiclechoice.model.entity.*;
import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.*;
import com.myvehiclechoice.service.DealerService;
import com.myvehiclechoice.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Admin Operations", description = "Catalogue CRUD, Price Management, CSV Importer, and Operational Stats")
public class AdminController {

    private final VehicleRepository vehicleRepository;
    private final VehicleVariantRepository variantRepository;
    private final BrandRepository brandRepository;
    private final DealerRepository dealerRepository;
    private final DealerEnquiryRepository enquiryRepository;
    private final DealerService dealerService;
    private final VehicleService vehicleService;

    public AdminController(VehicleRepository vehicleRepository,
                           VehicleVariantRepository variantRepository,
                           BrandRepository brandRepository,
                           DealerRepository dealerRepository,
                           DealerEnquiryRepository enquiryRepository,
                           DealerService dealerService,
                           VehicleService vehicleService) {
        this.vehicleRepository = vehicleRepository;
        this.variantRepository = variantRepository;
        this.brandRepository = brandRepository;
        this.dealerRepository = dealerRepository;
        this.enquiryRepository = enquiryRepository;
        this.dealerService = dealerService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get admin operations dashboard telemetry and catalogue health metrics")
    public ResponseEntity<ApiResponse<AdminStatsDto>> getStats() {
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalVehicles(vehicleRepository.count());
        stats.setTotalVariants(variantRepository.count());
        stats.setTotalBrands(brandRepository.count());
        stats.setTotalDealers(dealerRepository.count());
        stats.setTotalEnquiries(enquiryRepository.count());
        stats.setPendingEnquiries(enquiryRepository.countByStatus("NEW") + enquiryRepository.countByStatus("CONTACT_PENDING"));

        Map<String, Long> categoryCounts = new HashMap<>();
        for (VehicleCategory cat : VehicleCategory.values()) {
            categoryCounts.put(cat.name(), (long) vehicleRepository.findByCategory(cat).size());
        }
        stats.setCountByCategory(categoryCounts);

        List<VehicleVariant> variants = variantRepository.findAll();
        long missingSafety = variants.stream().filter(v -> v.getSafetyRatingStars() == null).count();
        long missingEff = variants.stream().filter(v -> v.getMileageKmpl() == null && v.getElectricRangeKm() == null).count();

        stats.setRecordsWithMissingSafetyRatings(missingSafety);
        stats.setRecordsWithMissingEfficiency(missingEff);

        return ResponseEntity.ok(ApiResponse.ok("Operational metrics retrieved", stats));
    }

    @GetMapping("/enquiries")
    @Operation(summary = "View all submitted customer enquiries")
    public ResponseEntity<ApiResponse<Page<DealerEnquiryResponse>>> getEnquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<DealerEnquiryResponse> result = dealerService.getAllEnquiries(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok("Enquiries retrieved", result));
    }

    @PatchMapping("/enquiries/{id}/status")
    @Operation(summary = "Update status of an enquiry")
    public ResponseEntity<ApiResponse<DealerEnquiryResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        DealerEnquiryResponse res = dealerService.updateEnquiryStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Enquiry status updated", res));
    }

    @PostMapping("/vehicles")
    @Operation(summary = "Create a new vehicle in the catalogue")
    public ResponseEntity<ApiResponse<VehicleDetailDto>> createVehicle(@RequestBody Map<String, Object> payload) {
        Long brandId = Long.parseLong(payload.get("brandId").toString());
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + brandId));

        Vehicle v = new Vehicle();
        v.setBrand(brand);
        v.setModelName(payload.get("modelName").toString());
        v.setCategory(VehicleCategory.valueOf(payload.get("category").toString().toUpperCase()));
        v.setBodyStyle((String) payload.get("bodyStyle"));
        v.setHeadline((String) payload.get("headline"));
        v.setDescription((String) payload.get("description"));
        v.setHeroImageUrl((String) payload.get("heroImageUrl"));
        v.setIsTrending(Boolean.parseBoolean(payload.getOrDefault("isTrending", "false").toString()));
        v.setIsFeatured(Boolean.parseBoolean(payload.getOrDefault("isFeatured", "false").toString()));

        Vehicle saved = vehicleRepository.save(v);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle created successfully", vehicleService.getVehicleById(saved.getId())));
    }

    @DeleteMapping("/vehicles/{id}")
    @Operation(summary = "Delete vehicle from catalogue")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found: " + id);
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle deleted successfully", null));
    }

    @PostMapping(value = "/import-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import vehicles or variants from CSV with validation preview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> importCsv(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int importedCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {
                try {
                    String brandName = record.get("Brand");
                    String modelName = record.get("Model");
                    String category = record.get("Category");
                    String variantName = record.get("Variant");
                    BigDecimal exShowroom = new BigDecimal(record.get("ExShowroomPrice"));
                    BigDecimal onRoad = new BigDecimal(record.get("EstimatedOnRoadPrice"));
                    String fuelType = record.get("FuelType");
                    String transmission = record.get("Transmission");

                    Brand brand = brandRepository.findByNameIgnoreCase(brandName)
                            .orElseGet(() -> brandRepository.save(new Brand(null, brandName, "India", null, true)));

                    Vehicle vehicle = vehicleRepository.findAll().stream()
                            .filter(v -> v.getBrand().getId().equals(brand.getId()) && v.getModelName().equalsIgnoreCase(modelName))
                            .findFirst()
                            .orElseGet(() -> {
                                Vehicle nv = new Vehicle();
                                nv.setBrand(brand);
                                nv.setModelName(modelName);
                                nv.setCategory(VehicleCategory.valueOf(category.toUpperCase()));
                                nv.setHeroImageUrl("https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=900&q=80");
                                return vehicleRepository.save(nv);
                            });

                    VehicleVariant variant = new VehicleVariant();
                    variant.setVehicle(vehicle);
                    variant.setVariantName(variantName);
                    variant.setModelYear(2026);
                    variant.setFuelType(FuelType.valueOf(fuelType.toUpperCase()));
                    variant.setTransmission(TransmissionType.valueOf(transmission.toUpperCase()));
                    variant.setExShowroomPrice(exShowroom);
                    variant.setEstimatedOnRoadPrice(onRoad);
                    variant.setCityPriceRef("New Delhi");
                    variant.setIsVerified(true);
                    variant.setDataSource("CSV Admin Import");

                    variantRepository.save(variant);
                    importedCount++;
                } catch (Exception ex) {
                    errors.add("Row " + record.getRecordNumber() + ": " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to parse CSV file: " + e.getMessage()));
        }

        result.put("importedRecords", importedCount);
        result.put("errorCount", errors.size());
        result.put("errors", errors);

        return ResponseEntity.ok(ApiResponse.ok("CSV import process completed", result));
    }
}
