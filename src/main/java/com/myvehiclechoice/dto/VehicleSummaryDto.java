package com.myvehiclechoice.dto;

import com.myvehiclechoice.model.enums.VehicleCategory;
import java.math.BigDecimal;
import java.util.List;

public class VehicleSummaryDto {
    private Long id;
    private Long brandId;
    private String brandName;
    private String modelName;
    private VehicleCategory category;
    private String bodyStyle;
    private String headline;
    private String heroImageUrl;
    private Boolean isTrending;
    private Boolean isFeatured;
    private BigDecimal startingExShowroomPrice;
    private BigDecimal startingOnRoadPrice;
    private BigDecimal maxOnRoadPrice;
    private List<String> availableFuelTypes;
    private List<String> availableTransmissions;
    private Integer variantCount;
    private String primaryKeySpec; // e.g. "24.8 kmpl" or "465 km Range" or "24.6 bhp"

    public VehicleSummaryDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public VehicleCategory getCategory() { return category; }
    public void setCategory(VehicleCategory category) { this.category = category; }

    public String getBodyStyle() { return bodyStyle; }
    public void setBodyStyle(String bodyStyle) { this.bodyStyle = bodyStyle; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getHeroImageUrl() { return heroImageUrl; }
    public void setHeroImageUrl(String heroImageUrl) { this.heroImageUrl = heroImageUrl; }

    public Boolean getIsTrending() { return isTrending; }
    public void setIsTrending(Boolean isTrending) { this.isTrending = isTrending; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public BigDecimal getStartingExShowroomPrice() { return startingExShowroomPrice; }
    public void setStartingExShowroomPrice(BigDecimal startingExShowroomPrice) { this.startingExShowroomPrice = startingExShowroomPrice; }

    public BigDecimal getStartingOnRoadPrice() { return startingOnRoadPrice; }
    public void setStartingOnRoadPrice(BigDecimal startingOnRoadPrice) { this.startingOnRoadPrice = startingOnRoadPrice; }

    public BigDecimal getMaxOnRoadPrice() { return maxOnRoadPrice; }
    public void setMaxOnRoadPrice(BigDecimal maxOnRoadPrice) { this.maxOnRoadPrice = maxOnRoadPrice; }

    public List<String> getAvailableFuelTypes() { return availableFuelTypes; }
    public void setAvailableFuelTypes(List<String> availableFuelTypes) { this.availableFuelTypes = availableFuelTypes; }

    public List<String> getAvailableTransmissions() { return availableTransmissions; }
    public void setAvailableTransmissions(List<String> availableTransmissions) { this.availableTransmissions = availableTransmissions; }

    public Integer getVariantCount() { return variantCount; }
    public void setVariantCount(Integer variantCount) { this.variantCount = variantCount; }

    public String getPrimaryKeySpec() { return primaryKeySpec; }
    public void setPrimaryKeySpec(String primaryKeySpec) { this.primaryKeySpec = primaryKeySpec; }
}
