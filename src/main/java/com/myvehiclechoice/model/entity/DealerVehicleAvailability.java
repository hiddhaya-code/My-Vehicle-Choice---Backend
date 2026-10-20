package com.myvehiclechoice.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dealer_vehicle_availability", uniqueConstraints = {
    @UniqueConstraint(name = "uk_dealer_vehicle", columnNames = {"dealer_id", "vehicle_id"})
})
public class DealerVehicleAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id", nullable = false)
    private Dealer dealer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private VehicleVariant variant;

    @Column(name = "availability_status", nullable = false, length = 50)
    private String availabilityStatus = "NOT_VERIFIED";

    @Column(name = "test_drive_available")
    private Boolean testDriveAvailable = false;

    @Column(name = "estimated_delivery_days")
    private Integer estimatedDeliveryDays;

    @Column(name = "last_verified_date")
    private LocalDate lastVerifiedDate;

    @Column(length = 255)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DealerVehicleAvailability() {}

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

    public Dealer getDealer() { return dealer; }
    public void setDealer(Dealer dealer) { this.dealer = dealer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public VehicleVariant getVariant() { return variant; }
    public void setVariant(VehicleVariant variant) { this.variant = variant; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public Boolean getTestDriveAvailable() { return testDriveAvailable; }
    public void setTestDriveAvailable(Boolean testDriveAvailable) { this.testDriveAvailable = testDriveAvailable; }

    public Integer getEstimatedDeliveryDays() { return estimatedDeliveryDays; }
    public void setEstimatedDeliveryDays(Integer estimatedDeliveryDays) { this.estimatedDeliveryDays = estimatedDeliveryDays; }

    public LocalDate getLastVerifiedDate() { return lastVerifiedDate; }
    public void setLastVerifiedDate(LocalDate lastVerifiedDate) { this.lastVerifiedDate = lastVerifiedDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
