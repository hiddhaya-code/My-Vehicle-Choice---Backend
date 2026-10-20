package com.myvehiclechoice.dto.calculator;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class EmiCalculationRequest {

    @NotNull(message = "Vehicle price is required")
    @DecimalMin(value = "10000.0", message = "Vehicle price must be at least ₹10,000")
    private BigDecimal vehiclePrice;

    @NotNull(message = "Down payment is required")
    @DecimalMin(value = "0.0", message = "Down payment cannot be negative")
    private BigDecimal downPayment;

    @NotNull(message = "Annual interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    private Double annualInterestRate = 9.5;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Tenure must be at least 6 months")
    private Integer tenureMonths = 60;

    public EmiCalculationRequest() {}

    public BigDecimal getVehiclePrice() { return vehiclePrice; }
    public void setVehiclePrice(BigDecimal vehiclePrice) { this.vehiclePrice = vehiclePrice; }

    public BigDecimal getDownPayment() { return downPayment; }
    public void setDownPayment(BigDecimal downPayment) { this.downPayment = downPayment; }

    public Double getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(Double annualInterestRate) { this.annualInterestRate = annualInterestRate; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
}
