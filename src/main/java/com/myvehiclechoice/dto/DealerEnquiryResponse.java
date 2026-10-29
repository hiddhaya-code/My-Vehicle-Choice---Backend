package com.myvehiclechoice.dto;

import java.time.LocalDateTime;

public class DealerEnquiryResponse {
    private Long id;
    private Long dealerId;
    private String dealerName;
    private String vehicleName;
    private String variantName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String city;
    private String enquiryType;
    private java.time.LocalDate preferredDate;
    private String message;
    private String status;
    private LocalDateTime createdAt;

    public DealerEnquiryResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDealerId() { return dealerId; }
    public void setDealerId(Long dealerId) { this.dealerId = dealerId; }

    public String getDealerName() { return dealerName; }
    public void setDealerName(String dealerName) { this.dealerName = dealerName; }

    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }

    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }

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

    public java.time.LocalDate getPreferredDate() { return preferredDate; }
    public void setPreferredDate(java.time.LocalDate preferredDate) { this.preferredDate = preferredDate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
