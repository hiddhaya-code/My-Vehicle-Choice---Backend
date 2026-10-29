package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.DealerEnquiryRequest;
import com.myvehiclechoice.dto.DealerEnquiryResponse;
import com.myvehiclechoice.dto.DealerResponseDto;
import com.myvehiclechoice.exception.ResourceNotFoundException;
import com.myvehiclechoice.model.entity.*;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DealerService {

    private final DealerRepository dealerRepository;
    private final DealerBrandRepository dealerBrandRepository;
    private final DealerVehicleAvailabilityRepository availabilityRepository;
    private final DealerEnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleVariantRepository variantRepository;
    private final BrandRepository brandRepository;

    public DealerService(DealerRepository dealerRepository,
                         DealerBrandRepository dealerBrandRepository,
                         DealerVehicleAvailabilityRepository availabilityRepository,
                         DealerEnquiryRepository enquiryRepository,
                         UserRepository userRepository,
                         VehicleRepository vehicleRepository,
                         VehicleVariantRepository variantRepository,
                         BrandRepository brandRepository) {
        this.dealerRepository = dealerRepository;
        this.dealerBrandRepository = dealerBrandRepository;
        this.availabilityRepository = availabilityRepository;
        this.enquiryRepository = enquiryRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.variantRepository = variantRepository;
        this.brandRepository = brandRepository;
    }

    /**
     * Accurate Dealer Search enforcing:
     * 1. Canonical manufacturer/brand matching.
     * 2. Vehicle category matching (e.g. HMSI Two-Wheelers vs Honda Cars India).
     * 3. Dealer capability filtering (exclude service-only workshops for vehicle purchases).
     * 4. Real Haversine distance calculation when coordinates provided.
     * 5. Separation of verified brand authorization from model inventory availability.
     */
    @Transactional(readOnly = true)
    public List<DealerResponseDto> getAccurateDealers(
            Long vehicleId,
            Long variantId,
            Long brandId,
            String brandName,
            VehicleCategory category,
            String dealerType,
            String city,
            String state,
            String pincode,
            Double userLat,
            Double userLng,
            Double radiusKm
    ) {
        Vehicle targetVehicle = null;
        Long targetBrandId = brandId;
        VehicleCategory targetCategory = category;

        // Step 1: Canonical Vehicle Resolution
        if (vehicleId != null) {
            targetVehicle = vehicleRepository.findById(vehicleId).orElse(null);
            if (targetVehicle != null) {
                targetBrandId = targetVehicle.getBrand().getId();
                targetCategory = targetVehicle.getCategory();
            }
        }

        // Canonical Brand Name Resolution if brandId was not directly passed
        if (targetBrandId == null && brandName != null && !brandName.trim().isEmpty()) {
            Optional<Brand> bOpt = brandRepository.findByNameIgnoreCase(brandName.trim());
            if (bOpt.isPresent()) {
                targetBrandId = bOpt.get().getId();
            }
        }

        // By default, for vehicle sales/purchases, require SALES or SALES_AND_SERVICE (exclude pure SERVICE)
        String effectiveDealerType = (dealerType != null && !dealerType.trim().isEmpty()) ? dealerType.trim().toUpperCase() : "SALES";

        String cleanCity = (city != null && !city.trim().isEmpty()) ? city.trim() : null;
        String cleanState = (state != null && !state.trim().isEmpty()) ? state.trim() : null;
        String cleanPincode = (pincode != null && !pincode.trim().isEmpty()) ? pincode.trim() : null;

        List<Dealer> rawDealers;

        if (targetBrandId != null) {
            // Enforce relational dealer_brands authorization
            if (cleanCity != null || cleanState != null || cleanPincode != null) {
                rawDealers = dealerRepository.findAuthorizedDealers(
                        targetBrandId,
                        targetCategory,
                        effectiveDealerType,
                        cleanCity,
                        cleanState,
                        cleanPincode
                );
            } else {
                rawDealers = dealerRepository.findAuthorizedDealersNoLocation(
                        targetBrandId,
                        targetCategory,
                        effectiveDealerType
                );
            }
        } else if (cleanCity != null || cleanState != null || cleanPincode != null) {
            rawDealers = dealerRepository.findDealersByLocation(cleanCity, cleanState, cleanPincode, effectiveDealerType);
        } else {
            rawDealers = dealerRepository.findByIsAuthorizedTrue().stream()
                    .filter(d -> effectiveDealerType == null || d.getDealerType().equals(effectiveDealerType) || d.getDealerType().equals("SALES_AND_SERVICE"))
                    .collect(Collectors.toList());
        }

        // Map and enrich dealers
        final Vehicle finalVehicle = targetVehicle;
        final Long finalBrandId = targetBrandId;
        final VehicleCategory finalCategory = targetCategory;

        List<DealerResponseDto> results = new ArrayList<>();

        for (Dealer dealer : rawDealers) {
            DealerResponseDto dto = new DealerResponseDto();
            dto.setId(dealer.getId());
            dto.setName(dealer.getName());
            dto.setBrandName(dealer.getBrandName());
            dto.setIsAuthorized(dealer.getIsAuthorized());
            dto.setDealerType(dealer.getDealerType());
            dto.setAddress(dealer.getAddress());
            dto.setLocality(dealer.getLocality());
            dto.setCity(dealer.getCity());
            dto.setState(dealer.getState());
            dto.setPincode(dealer.getPincode());
            dto.setLatitude(dealer.getLatitudeDouble());
            dto.setLongitude(dealer.getLongitudeDouble());
            dto.setPhone(dealer.getPhone());
            dto.setWebsite(dealer.getWebsite());
            dto.setOfficialLocatorUrl(dealer.getOfficialLocatorUrl());
            dto.setBusinessHours(dealer.getBusinessHours());
            dto.setVerifiedSource(dealer.getVerifiedSource());
            dto.setVerifiedDate(dealer.getVerifiedDate());
            dto.setVerificationStatus(dealer.getVerificationStatus());

            // Set brand ID & category from resolved target or dealer_brands
            if (finalBrandId != null) {
                dto.setBrandId(finalBrandId);
            }
            if (finalCategory != null) {
                dto.setVehicleCategory(finalCategory.name());
            }

            // Find matching dealer_brands metadata (e.g. division name, official brand locator fallback)
            List<DealerBrand> dbs = dealerBrandRepository.findByDealerId(dealer.getId());
            if (!dbs.isEmpty()) {
                DealerBrand matchedDb = dbs.stream()
                        .filter(db -> (finalBrandId == null || db.getBrand().getId().equals(finalBrandId))
                                && (finalCategory == null || db.getVehicleCategory().equals(finalCategory)))
                        .findFirst()
                        .orElse(dbs.get(0));

                dto.setBrandId(matchedDb.getBrand().getId());
                dto.setBrandName(matchedDb.getBrand().getName());
                dto.setDivisionName(matchedDb.getDivisionName());
                dto.setVehicleCategory(matchedDb.getVehicleCategory().name());

                if ((dto.getOfficialLocatorUrl() == null || dto.getOfficialLocatorUrl().isEmpty())
                        && matchedDb.getBrand().getOfficialDealerLocatorUrl() != null) {
                    dto.setOfficialLocatorUrl(matchedDb.getBrand().getOfficialDealerLocatorUrl());
                }
            }

            // Model Availability Check
            if (finalVehicle != null) {
                Optional<DealerVehicleAvailability> dvaOpt = availabilityRepository.findByDealerIdAndVehicleId(dealer.getId(), finalVehicle.getId());
                if (dvaOpt.isPresent()) {
                    DealerVehicleAvailability dva = dvaOpt.get();
                    dto.setAvailabilityStatus(dva.getAvailabilityStatus());
                    dto.setTestDriveAvailable(Boolean.TRUE.equals(dva.getTestDriveAvailable()));
                    dto.setEstimatedDeliveryDays(dva.getEstimatedDeliveryDays());
                } else {
                    dto.setAvailabilityStatus("NOT_VERIFIED");
                    dto.setTestDriveAvailable(false);
                    dto.setEstimatedDeliveryDays(null);
                }
            } else {
                dto.setAvailabilityStatus("NOT_VERIFIED");
                dto.setTestDriveAvailable(false);
                dto.setEstimatedDeliveryDays(null);
            }

            // Geographic Distance Calculation (Haversine Formula)
            if (userLat != null && userLng != null && dealer.getLatitudeDouble() != null && dealer.getLongitudeDouble() != null) {
                double distance = calculateHaversineDistanceKm(userLat, userLng, dealer.getLatitudeDouble(), dealer.getLongitudeDouble());
                dto.setDistanceKm(Math.round(distance * 10.0) / 10.0);

                // Filter by radius if specified
                if (radiusKm != null && radiusKm > 0 && distance > radiusKm) {
                    continue; // Skip dealer outside radius
                }
            } else {
                dto.setDistanceKm(null);
            }

            results.add(dto);
        }

        // Sort by distance if calculated, otherwise keep verified dealers prioritized
        if (userLat != null && userLng != null) {
            results.sort(Comparator.comparing(
                    d -> d.getDistanceKm() != null ? d.getDistanceKm() : Double.MAX_VALUE
            ));
        }

        return results;
    }

    /**
     * Backward-compatible simple getDealers query.
     */
    @Transactional(readOnly = true)
    public List<Dealer> getDealers(String city, String brandName) {
        if ((city == null || city.trim().isEmpty()) && (brandName == null || brandName.trim().isEmpty())) {
            return dealerRepository.findAll();
        }
        return dealerRepository.findDealers(
                (city != null && !city.trim().isEmpty()) ? city.trim() : null,
                (brandName != null && !brandName.trim().isEmpty()) ? brandName.trim() : null
        );
    }

    private double calculateHaversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public DealerEnquiryResponse createEnquiry(DealerEnquiryRequest req, String userEmail) {
        if (req.getVehicleId() == null) {
            throw new IllegalArgumentException("A valid vehicle ID is required to submit a dealer enquiry.");
        }
        if (req.getDealerId() == null) {
            throw new IllegalArgumentException("A valid dealer ID is required to submit a dealer enquiry.");
        }

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + req.getVehicleId()));

        Dealer dealer = dealerRepository.findById(req.getDealerId())
                .orElseThrow(() -> new ResourceNotFoundException("Dealer not found with ID: " + req.getDealerId()));

        // Enforce Dealer Capability: Service-only workshops cannot accept vehicle purchase / test-drive enquiries
        if ("SERVICE".equalsIgnoreCase(dealer.getDealerType())) {
            throw new IllegalArgumentException(
                    "Selected location '" + dealer.getName() + "' is an authorized service workshop only, not a vehicle sales showroom."
            );
        }

        // Enforce Strict Manufacturer & Category Authorization Match: No cross-brand leakage
        List<DealerBrand> authorizedBrands = dealerBrandRepository.findByDealerId(dealer.getId());
        boolean isBrandAuthorized = authorizedBrands.stream().anyMatch(db ->
                db.getBrand().getId().equals(vehicle.getBrand().getId()) &&
                Boolean.TRUE.equals(db.getIsAuthorized()) &&
                (db.getVehicleCategory() == null || db.getVehicleCategory().equals(vehicle.getCategory()))
        );

        // Fallback check against dealer.getBrandName() if relational dealer_brands was not mapped
        if (!isBrandAuthorized && authorizedBrands.isEmpty()) {
            isBrandAuthorized = dealer.getBrandName() != null &&
                    dealer.getBrandName().equalsIgnoreCase(vehicle.getBrand().getName());
        }

        if (!isBrandAuthorized) {
            throw new IllegalArgumentException(
                    "Selected dealer '" + dealer.getName() + "' is not an authorized sales partner for " +
                    vehicle.getBrand().getName() + " (" + vehicle.getCategory() + ")."
            );
        }

        // Validate Preferred Date (Must not be in the past)
        if (req.getPreferredDate() != null) {
            if (req.getPreferredDate().isBefore(java.time.LocalDate.now())) {
                throw new IllegalArgumentException("Preferred test-drive date cannot be in the past.");
            }
        }

        DealerEnquiry enquiry = new DealerEnquiry();
        enquiry.setDealer(dealer);
        enquiry.setVehicle(vehicle);

        if (req.getVariantId() != null) {
            variantRepository.findById(req.getVariantId()).ifPresent(enquiry::setVariant);
        } else if (vehicle.getVariants() != null && !vehicle.getVariants().isEmpty()) {
            enquiry.setVariant(vehicle.getVariants().get(0));
        }

        if (userEmail != null) {
            userRepository.findByEmailIgnoreCase(userEmail).ifPresent(enquiry::setUser);
        }

        enquiry.setCustomerName(req.getCustomerName());
        enquiry.setCustomerEmail(req.getCustomerEmail());
        enquiry.setCustomerPhone(req.getCustomerPhone());
        enquiry.setCity(req.getCity());
        enquiry.setEnquiryType(req.getEnquiryType());
        enquiry.setPreferredDate(req.getPreferredDate());
        enquiry.setMessage(req.getMessage());
        enquiry.setStatus("NEW");

        DealerEnquiry saved = enquiryRepository.save(enquiry);
        return toEnquiryResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DealerEnquiryResponse> getUserEnquiries(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return enquiryRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toEnquiryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DealerEnquiryResponse> getAllEnquiries(Pageable pageable) {
        return enquiryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toEnquiryResponse);
    }

    public DealerEnquiryResponse updateEnquiryStatus(Long id, String status) {
        DealerEnquiry enquiry = enquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found: " + id));

        enquiry.setStatus(status.toUpperCase());
        DealerEnquiry saved = enquiryRepository.save(enquiry);
        return toEnquiryResponse(saved);
    }

    private DealerEnquiryResponse toEnquiryResponse(DealerEnquiry e) {
        DealerEnquiryResponse dto = new DealerEnquiryResponse();
        dto.setId(e.getId());
        if (e.getDealer() != null) {
            dto.setDealerId(e.getDealer().getId());
            dto.setDealerName(e.getDealer().getName());
        }
        if (e.getVehicle() != null) {
            dto.setVehicleName(e.getVehicle().getBrand().getName() + " " + e.getVehicle().getModelName());
        }
        if (e.getVariant() != null) {
            dto.setVariantName(e.getVariant().getVariantName());
        }
        dto.setCustomerName(e.getCustomerName());
        dto.setCustomerEmail(e.getCustomerEmail());
        dto.setCustomerPhone(e.getCustomerPhone());
        dto.setCity(e.getCity());
        dto.setEnquiryType(e.getEnquiryType());
        dto.setPreferredDate(e.getPreferredDate());
        dto.setMessage(e.getMessage());
        dto.setStatus(e.getStatus());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}
