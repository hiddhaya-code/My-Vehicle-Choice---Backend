package com.myvehiclechoice.dto;

import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import java.math.BigDecimal;

public class CompareSearchResultDto {
    private Long vehicleId;
    private Long variantId;
    private String brandName;
    private String modelName;
    private String variantName;
    private String fullName; // e.g. "Tata Nexon Pure 1.2 Revotron Petrol 6MT"
    private VehicleCategory category;
    private FuelType fuelType;
    private TransmissionType transmission;
    private BigDecimal estimatedOnRoadPrice;
    private String heroImageUrl;

    public CompareSearchResultDto() {}

    public CompareSearchResultDto(Long vehicleId, Long variantId, String brandName, String modelName, 
                                  String variantName, VehicleCategory category, FuelType fuelType, 
                                  TransmissionType transmission, BigDecimal estimatedOnRoadPrice, String heroImageUrl) {
        this.vehicleId = vehicleId;
        this.variantId = variantId;
        this.brandName = brandName;
        this.modelName = modelName;
        this.variantName = variantName;
        this.fullName = brandName + " " + modelName + " " + variantName;
        this.category = category;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.estimatedOnRoadPrice = estimatedOnRoadPrice;
        this.heroImageUrl = heroImageUrl;
    }

    // Getters & Setters
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public VehicleCategory getCategory() { return category; }
    public void setCategory(VehicleCategory category) { this.category = category; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    public TransmissionType getTransmission() { return transmission; }
    public void setTransmission(TransmissionType transmission) { this.transmission = transmission; }

    public BigDecimal getEstimatedOnRoadPrice() { return estimatedOnRoadPrice; }
    public void setEstimatedOnRoadPrice(BigDecimal estimatedOnRoadPrice) { this.estimatedOnRoadPrice = estimatedOnRoadPrice; }

    public String getHeroImageUrl() { return heroImageUrl; }
    public void setHeroImageUrl(String heroImageUrl) { this.heroImageUrl = heroImageUrl; }
}
