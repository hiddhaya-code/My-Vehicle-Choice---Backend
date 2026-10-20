package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.*;
import com.myvehiclechoice.exception.ResourceNotFoundException;
import com.myvehiclechoice.model.entity.Vehicle;
import com.myvehiclechoice.model.entity.VehicleImage;
import com.myvehiclechoice.model.entity.VehicleVariant;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.VehicleImageRepository;
import com.myvehiclechoice.repository.VehicleRepository;
import com.myvehiclechoice.repository.VehicleVariantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleVariantRepository variantRepository;
    private final VehicleImageRepository imageRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          VehicleVariantRepository variantRepository,
                          VehicleImageRepository imageRepository) {
        this.vehicleRepository = vehicleRepository;
        this.variantRepository = variantRepository;
        this.imageRepository = imageRepository;
    }

    public List<VehicleSummaryDto> getTrendingVehicles() {
        return vehicleRepository.findByIsTrendingTrue()
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public List<VehicleSummaryDto> getFeaturedVehicles() {
        return vehicleRepository.findByIsFeaturedTrue()
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public List<VehicleSummaryDto> getVehiclesByCategory(VehicleCategory category) {
        return vehicleRepository.findByCategory(category)
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    public PageResponse<VehicleSummaryDto> filterVehicles(VehicleFilterCriteria criteria) {
        Sort sort = Sort.by(
                criteria.getSortDir().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                criteria.getSortBy().equalsIgnoreCase("price") ? "id" : criteria.getSortBy()
        );
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Page<Vehicle> page;
        if (criteria.getQuery() != null && !criteria.getQuery().trim().isEmpty()) {
            List<Vehicle> matches = vehicleRepository.searchByQuery(criteria.getQuery().trim(), pageable);
            List<VehicleSummaryDto> dtoList = matches.stream().map(this::toSummaryDto).collect(Collectors.toList());
            return new PageResponse<>(dtoList, criteria.getPage(), criteria.getSize(), matches.size(), 1, true);
        } else {
            page = vehicleRepository.filterVehicles(
                    criteria.getCategory(),
                    criteria.getBrandId(),
                    criteria.getBodyStyle(),
                    criteria.getMinPrice(),
                    criteria.getMaxPrice(),
                    pageable
            );
        }

        List<VehicleSummaryDto> dtoList = page.getContent()
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public VehicleDetailDto getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

        List<VehicleVariant> variants = variantRepository.findByVehicleIdOrderByEstimatedOnRoadPriceAsc(id);
        List<VehicleImage> images = imageRepository.findByVehicleIdOrderByDisplayOrderAsc(id);

        return toDetailDto(vehicle, variants, images);
    }

    public List<CompareSearchResultDto> searchVariantsForCompare(String query, int limit) {
        if (query == null || query.trim().length() < 2) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(0, Math.min(limit, 20));
        List<VehicleVariant> variants = variantRepository.searchVariantsForCompare(query.trim(), pageable);

        return variants.stream()
                .map(v -> new CompareSearchResultDto(
                        v.getVehicle().getId(),
                        v.getId(),
                        v.getVehicle().getBrand().getName(),
                        v.getVehicle().getModelName(),
                        v.getVariantName(),
                        v.getVehicle().getCategory(),
                        v.getFuelType(),
                        v.getTransmission(),
                        v.getEstimatedOnRoadPrice(),
                        v.getVehicle().getHeroImageUrl()
                ))
                .collect(Collectors.toList());
    }

    public VehicleSummaryDto toSummaryDto(Vehicle vehicle) {
        VehicleSummaryDto dto = new VehicleSummaryDto();
        dto.setId(vehicle.getId());
        dto.setBrandId(vehicle.getBrand().getId());
        dto.setBrandName(vehicle.getBrand().getName());
        dto.setModelName(vehicle.getModelName());
        dto.setCategory(vehicle.getCategory());
        dto.setBodyStyle(vehicle.getBodyStyle());
        dto.setHeadline(vehicle.getHeadline());
        dto.setHeroImageUrl(vehicle.getHeroImageUrl());
        dto.setIsTrending(vehicle.getIsTrending());
        dto.setIsFeatured(vehicle.getIsFeatured());

        List<VehicleVariant> variants = variantRepository.findByVehicleIdOrderByEstimatedOnRoadPriceAsc(vehicle.getId());
        dto.setVariantCount(variants.size());

        if (!variants.isEmpty()) {
            dto.setStartingExShowroomPrice(variants.get(0).getExShowroomPrice());
            dto.setStartingOnRoadPrice(variants.get(0).getEstimatedOnRoadPrice());
            dto.setMaxOnRoadPrice(variants.get(variants.size() - 1).getEstimatedOnRoadPrice());

            dto.setAvailableFuelTypes(
                    variants.stream()
                            .map(v -> v.getFuelType().name())
                            .distinct()
                            .collect(Collectors.toList())
            );

            dto.setAvailableTransmissions(
                    variants.stream()
                            .map(v -> v.getTransmission().name())
                            .distinct()
                            .collect(Collectors.toList())
            );

            // Compute primary highlight key spec
            VehicleVariant topVar = variants.get(0);
            if (topVar.getElectricRangeKm() != null && topVar.getElectricRangeKm() > 0) {
                dto.setPrimaryKeySpec(topVar.getElectricRangeKm() + " km Range");
            } else if (topVar.getMileageKmpl() != null && topVar.getMileageKmpl().compareTo(BigDecimal.ZERO) > 0) {
                dto.setPrimaryKeySpec(topVar.getMileageKmpl() + " kmpl Mileage");
            } else if (topVar.getMaxPowerBhp() != null) {
                dto.setPrimaryKeySpec(topVar.getMaxPowerBhp() + " bhp Power");
            } else {
                dto.setPrimaryKeySpec("Verified Indian Spec");
            }
        }

        return dto;
    }

    public VehicleDetailDto toDetailDto(Vehicle vehicle, List<VehicleVariant> variants, List<VehicleImage> images) {
        VehicleDetailDto dto = new VehicleDetailDto();
        dto.setId(vehicle.getId());
        dto.setBrandId(vehicle.getBrand().getId());
        dto.setBrandName(vehicle.getBrand().getName());
        dto.setBrandLogoUrl(vehicle.getBrand().getLogoUrl());
        dto.setBrandCountry(vehicle.getBrand().getCountry());
        dto.setModelName(vehicle.getModelName());
        dto.setCategory(vehicle.getCategory());
        dto.setBodyStyle(vehicle.getBodyStyle());
        dto.setHeadline(vehicle.getHeadline());
        dto.setDescription(vehicle.getDescription());
        dto.setHeroImageUrl(vehicle.getHeroImageUrl());
        dto.setIsTrending(vehicle.getIsTrending());
        dto.setIsFeatured(vehicle.getIsFeatured());

        if (!variants.isEmpty()) {
            dto.setStartingExShowroomPrice(variants.get(0).getExShowroomPrice());
            dto.setStartingOnRoadPrice(variants.get(0).getEstimatedOnRoadPrice());
            dto.setMaxOnRoadPrice(variants.get(variants.size() - 1).getEstimatedOnRoadPrice());
        }

        dto.setVariants(variants.stream().map(this::toVariantDto).collect(Collectors.toList()));
        dto.setImages(images.stream().map(this::toImageDto).collect(Collectors.toList()));

        return dto;
    }

    public VehicleVariantDto toVariantDto(VehicleVariant v) {
        VehicleVariantDto dto = new VehicleVariantDto();
        dto.setId(v.getId());
        dto.setVehicleId(v.getVehicle().getId());
        dto.setVariantName(v.getVariantName());
        dto.setModelYear(v.getModelYear());
        dto.setFuelType(v.getFuelType());
        dto.setTransmission(v.getTransmission());
        dto.setExShowroomPrice(v.getExShowroomPrice());
        dto.setEstimatedOnRoadPrice(v.getEstimatedOnRoadPrice());
        dto.setCityPriceRef(v.getCityPriceRef());
        dto.setMileageKmpl(v.getMileageKmpl());
        dto.setElectricRangeKm(v.getElectricRangeKm());
        dto.setBatteryCapacityKwh(v.getBatteryCapacityKwh());
        dto.setEngineCc(v.getEngineCc());
        dto.setMaxPowerBhp(v.getMaxPowerBhp());
        dto.setMaxPowerRpm(v.getMaxPowerRpm());
        dto.setMaxTorqueNm(v.getMaxTorqueNm());
        dto.setSeatingCapacity(v.getSeatingCapacity());
        dto.setBootSpaceLitres(v.getBootSpaceLitres());
        dto.setGroundClearanceMm(v.getGroundClearanceMm());
        dto.setFuelTankCapacityLitres(v.getFuelTankCapacityLitres());
        dto.setKerbWeightKg(v.getKerbWeightKg());
        dto.setSeatHeightMm(v.getSeatHeightMm());
        dto.setSafetyRatingStars(v.getSafetyRatingStars());
        dto.setSafetyRatingProtocol(v.getSafetyRatingProtocol());
        dto.setAirbagsCount(v.getAirbagsCount());
        dto.setHasAbs(v.getHasAbs());
        dto.setHasEsp(v.getHasEsp());
        dto.setMaintenanceEstimateAnnual(v.getMaintenanceEstimateAnnual());
        dto.setKeyAdvantages(v.getKeyAdvantages());
        dto.setKeyDisadvantages(v.getKeyDisadvantages());
        dto.setIsVerified(v.getIsVerified());
        dto.setDataSource(v.getDataSource());
        dto.setVerifiedDate(v.getVerifiedDate());
        return dto;
    }

    public VehicleImageDto toImageDto(VehicleImage img) {
        return new VehicleImageDto(
                img.getId(),
                img.getImageUrl(),
                img.getCaption(),
                img.getIsPrimary(),
                img.getDisplayOrder()
        );
    }
}
