package com.myvehiclechoice.dto;

import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import java.math.BigDecimal;

public class VehicleFilterCriteria {
    private String query;
    private VehicleCategory category;
    private Long brandId;
    private FuelType fuelType;
    private TransmissionType transmission;
    private String bodyStyle;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean isFeatured;
    private Boolean isTrending;
    private int page = 0;
    private int size = 12;
    private String sortBy = "id";
    private String sortDir = "ASC";

    public VehicleFilterCriteria() {}

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public VehicleCategory getCategory() { return category; }
    public void setCategory(VehicleCategory category) { this.category = category; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    public TransmissionType getTransmission() { return transmission; }
    public void setTransmission(TransmissionType transmission) { this.transmission = transmission; }

    public String getBodyStyle() { return bodyStyle; }
    public void setBodyStyle(String bodyStyle) { this.bodyStyle = bodyStyle; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public Boolean getIsTrending() { return isTrending; }
    public void setIsTrending(Boolean isTrending) { this.isTrending = isTrending; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDir() { return sortDir; }
    public void setSortDir(String sortDir) { this.sortDir = sortDir; }
}
