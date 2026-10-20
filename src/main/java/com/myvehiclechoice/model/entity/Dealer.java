package com.myvehiclechoice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dealers")
public class Dealer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "brand_name", nullable = false, length = 100)
    private String brandName;

    @Column(name = "is_authorized")
    private Boolean isAuthorized = true;

    @Column(name = "dealer_type", nullable = false, length = 30)
    private String dealerType = "SALES";

    @Column(name = "verification_status", nullable = false, length = 50)
    private String verificationStatus = "OFFICIAL_OEM_VERIFIED";

    @Column(nullable = false, length = 300)
    private String address;

    @Column(length = 150)
    private String locality;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 20)
    private String pincode;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(length = 300)
    private String website;

    @Column(name = "official_locator_url", length = 300)
    private String officialLocatorUrl;

    @Column(name = "business_hours", length = 150)
    private String businessHours;

    @Column(name = "verified_source", length = 200)
    private String verifiedSource;

    @Column(name = "verified_date")
    private LocalDate verifiedDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "dealer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DealerBrand> dealerBrands = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "dealer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DealerVehicleAvailability> availabilities = new ArrayList<>();

    public Dealer() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public Boolean getIsAuthorized() { return isAuthorized; }
    public void setIsAuthorized(Boolean isAuthorized) { this.isAuthorized = isAuthorized; }

    public String getDealerType() { return dealerType; }
    public void setDealerType(String dealerType) { this.dealerType = dealerType; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

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

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude != null ? BigDecimal.valueOf(latitude) : null; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude != null ? BigDecimal.valueOf(longitude) : null; }

    public Double getLatitudeDouble() { return latitude != null ? latitude.doubleValue() : null; }
    public Double getLongitudeDouble() { return longitude != null ? longitude.doubleValue() : null; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<DealerBrand> getDealerBrands() { return dealerBrands; }
    public void setDealerBrands(List<DealerBrand> dealerBrands) { this.dealerBrands = dealerBrands; }

    public List<DealerVehicleAvailability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(List<DealerVehicleAvailability> availabilities) { this.availabilities = availabilities; }
}
