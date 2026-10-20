package com.myvehiclechoice.dto;

import java.time.LocalDate;

public class DealerResponseDto {

    private Long id;
    private String name;
    private String brandName;
    private Long brandId;
    private String divisionName;
    private Boolean isAuthorized;
    private String dealerType;
    private String address;
    private String locality;
    private String city;
    private String state;
    private String pincode;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;
    private String officialLocatorUrl;
    private String businessHours;
    private String verifiedSource;
    private LocalDate verifiedDate;
    private String verificationStatus;
    private Double distanceKm;
    private String availabilityStatus;
    private Boolean testDriveAvailable;
    private Integer estimatedDeliveryDays;
    private String vehicleCategory;

    public DealerResponseDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public String getDivisionName() { return divisionName; }
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    public Boolean getIsAuthorized() { return isAuthorized; }
    public void setIsAuthorized(Boolean isAuthorized) { this.isAuthorized = isAuthorized; }

    public String getDealerType() { return dealerType; }
    public void setDealerType(String dealerType) { this.dealerType = dealerType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getOfficialLocatorUrl() { return officialLocatorUrl; }
    public void setOfficialLocatorUrl(String officialLocatorUrl) { this.officialLocatorUrl = officialLocatorUrl; }

    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }

    public String getVerifiedSource() { return verifiedSource; }
    public void setVerifiedSource(String verifiedSource) { this.verifiedSource = verifiedSource; }

    public LocalDate getVerifiedDate() { return verifiedDate; }
    public void setVerifiedDate(LocalDate verifiedDate) { this.verifiedDate = verifiedDate; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public Boolean getTestDriveAvailable() { return testDriveAvailable; }
    public void setTestDriveAvailable(Boolean testDriveAvailable) { this.testDriveAvailable = testDriveAvailable; }

    public Integer getEstimatedDeliveryDays() { return estimatedDeliveryDays; }
    public void setEstimatedDeliveryDays(Integer estimatedDeliveryDays) { this.estimatedDeliveryDays = estimatedDeliveryDays; }

    public String getVehicleCategory() { return vehicleCategory; }
    public void setVehicleCategory(String vehicleCategory) { this.vehicleCategory = vehicleCategory; }
}
