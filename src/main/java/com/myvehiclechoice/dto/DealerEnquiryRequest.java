package com.myvehiclechoice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DealerEnquiryRequest {
    private Long dealerId;
    private Long vehicleId;
    private Long variantId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Phone number is required")
    private String customerPhone;

    @NotBlank(message = "City is required")
    private String city = "New Delhi";

    @NotBlank(message = "Enquiry type is required")
    private String enquiryType = "TEST_DRIVE"; // TEST_DRIVE, PRICE_QUOTE, AVAILABILITY, CONTACT_REQUEST

    private String message;

    public DealerEnquiryRequest() {}

    public Long getDealerId() { return dealerId; }
    public void setDealerId(Long dealerId) { this.dealerId = dealerId; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }

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
}
