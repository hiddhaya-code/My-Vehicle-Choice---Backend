package com.myvehiclechoice.model.entity;

import com.myvehiclechoice.model.enums.VehicleCategory;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dealer_brands", uniqueConstraints = {
    @UniqueConstraint(name = "uk_dealer_brand_cat", columnNames = {"dealer_id", "brand_id", "vehicle_category"})
})
public class DealerBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id", nullable = false)
    private Dealer dealer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_category", nullable = false)
    private VehicleCategory vehicleCategory;

    @Column(name = "is_authorized")
    private Boolean isAuthorized = true;

    @Column(name = "division_name", length = 150)
    private String divisionName;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public DealerBrand() {}

    public DealerBrand(Dealer dealer, Brand brand, VehicleCategory vehicleCategory, Boolean isAuthorized, String divisionName) {
        this.dealer = dealer;
        this.brand = brand;
        this.vehicleCategory = vehicleCategory;
        this.isAuthorized = isAuthorized;
        this.divisionName = divisionName;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dealer getDealer() { return dealer; }
    public void setDealer(Dealer dealer) { this.dealer = dealer; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public VehicleCategory getVehicleCategory() { return vehicleCategory; }
    public void setVehicleCategory(VehicleCategory vehicleCategory) { this.vehicleCategory = vehicleCategory; }

    public Boolean getIsAuthorized() { return isAuthorized; }
    public void setIsAuthorized(Boolean isAuthorized) { this.isAuthorized = isAuthorized; }

    public String getDivisionName() { return divisionName; }
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
