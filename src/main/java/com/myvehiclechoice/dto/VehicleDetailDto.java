package com.myvehiclechoice.dto;

import com.myvehiclechoice.model.enums.VehicleCategory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VehicleDetailDto {
    private Long id;
    private Long brandId;
    private String brandName;
    private String brandLogoUrl;
    private String brandCountry;
    private String modelName;
    private VehicleCategory category;
    private String bodyStyle;
    private String headline;
    private String description;
    private String heroImageUrl;
    private Boolean isTrending;
    private Boolean isFeatured;
    private BigDecimal startingExShowroomPrice;
    private BigDecimal startingOnRoadPrice;
    private BigDecimal maxOnRoadPrice;
    private List<VehicleVariantDto> variants = new ArrayList<>();
    private List<VehicleImageDto> images = new ArrayList<>();

    public VehicleDetailDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getBrandLogoUrl() { return brandLogoUrl; }
    public void setBrandLogoUrl(String brandLogoUrl) { this.brandLogoUrl = brandLogoUrl; }

    public String getBrandCountry() { return brandCountry; }
    public void setBrandCountry(String brandCountry) { this.brandCountry = brandCountry; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public VehicleCategory getCategory() { return category; }
    public void setCategory(VehicleCategory category) { this.category = category; }

    public String getBodyStyle() { return bodyStyle; }
    public void setBodyStyle(String bodyStyle) { this.bodyStyle = bodyStyle; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

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

    public List<VehicleVariantDto> getVariants() { return variants; }
    public void setVariants(List<VehicleVariantDto> variants) { this.variants = variants; }

    public List<VehicleImageDto> getImages() { return images; }
    public void setImages(List<VehicleImageDto> images) { this.images = images; }
}
