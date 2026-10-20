package com.myvehiclechoice.dto.recommendation;

import java.math.BigDecimal;

public class OwnershipAffordabilityDto {

    private BigDecimal exShowroomPrice;
    private BigDecimal onRoadPrice;
    private BigDecimal downPayment;
    private BigDecimal loanAmount;
    private BigDecimal monthlyEmi;
    private BigDecimal monthlyFuelOrEnergyCost;
    private BigDecimal annualMaintenanceEstimate;
    private BigDecimal annualInsuranceEstimate;
    private BigDecimal fiveYearTotalCostOfOwnership;
    private String calculationAssumptions;

    public OwnershipAffordabilityDto() {}

    public BigDecimal getExShowroomPrice() { return exShowroomPrice; }
    public void setExShowroomPrice(BigDecimal exShowroomPrice) { this.exShowroomPrice = exShowroomPrice; }

    public BigDecimal getOnRoadPrice() { return onRoadPrice; }
    public void setOnRoadPrice(BigDecimal onRoadPrice) { this.onRoadPrice = onRoadPrice; }

    public BigDecimal getDownPayment() { return downPayment; }
    public void setDownPayment(BigDecimal downPayment) { this.downPayment = downPayment; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public BigDecimal getMonthlyEmi() { return monthlyEmi; }
    public void setMonthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; }

    public BigDecimal getMonthlyFuelOrEnergyCost() { return monthlyFuelOrEnergyCost; }
    public void setMonthlyFuelOrEnergyCost(BigDecimal monthlyFuelOrEnergyCost) { this.monthlyFuelOrEnergyCost = monthlyFuelOrEnergyCost; }

    public BigDecimal getAnnualMaintenanceEstimate() { return annualMaintenanceEstimate; }
    public void setAnnualMaintenanceEstimate(BigDecimal annualMaintenanceEstimate) { this.annualMaintenanceEstimate = annualMaintenanceEstimate; }

    public BigDecimal getAnnualInsuranceEstimate() { return annualInsuranceEstimate; }
    public void setAnnualInsuranceEstimate(BigDecimal annualInsuranceEstimate) { this.annualInsuranceEstimate = annualInsuranceEstimate; }

    public BigDecimal getFiveYearTotalCostOfOwnership() { return fiveYearTotalCostOfOwnership; }
    public void setFiveYearTotalCostOfOwnership(BigDecimal fiveYearTotalCostOfOwnership) { this.fiveYearTotalCostOfOwnership = fiveYearTotalCostOfOwnership; }

    public String getCalculationAssumptions() { return calculationAssumptions; }
    public void setCalculationAssumptions(String calculationAssumptions) { this.calculationAssumptions = calculationAssumptions; }
}
