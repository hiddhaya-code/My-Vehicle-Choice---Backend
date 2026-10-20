package com.myvehiclechoice.dto;

public class VehicleImageDto {
    private Long id;
    private String imageUrl;
    private String caption;
    private Boolean isPrimary;
    private Integer displayOrder;

    public VehicleImageDto() {}

    public VehicleImageDto(Long id, String imageUrl, String caption, Boolean isPrimary, Integer displayOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.isPrimary = isPrimary;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
