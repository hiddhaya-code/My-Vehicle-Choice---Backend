package com.myvehiclechoice.dto;

public class BrandDto {
    private Long id;
    private String name;
    private String country;
    private String logoUrl;
    private Boolean isActive;

    public BrandDto() {}

    public BrandDto(Long id, String name, String country, String logoUrl, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.logoUrl = logoUrl;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
