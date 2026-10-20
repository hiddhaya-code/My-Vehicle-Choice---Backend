package com.myvehiclechoice.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dealer_enquiries")
public class DealerEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private VehicleVariant variant;

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 150)
    private String customerEmail;

    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "enquiry_type", nullable = false, length = 50)
    private String enquiryType; // TEST_DRIVE, PRICE_QUOTE, AVAILABILITY, CONTACT_REQUEST

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 50)
    private String status = "NEW"; // NEW, CONTACT_PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DealerEnquiry() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Dealer getDealer() { return dealer; }
    public void setDealer(Dealer dealer) { this.dealer = dealer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public VehicleVariant getVariant() { return variant; }
    public void setVariant(VehicleVariant variant) { this.variant = variant; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getEnquiryType() { return enquiryType; }
    public void setEnquiryType(String enquiryType) { this.enquiryType = enquiryType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
