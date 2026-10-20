package com.myvehiclechoice.dto.calculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class OwnershipCostResult {
    private Integer tenureYears; // 3 or 5
    private BigDecimal purchasePrice;
    private BigDecimal financingInterest;
    private BigDecimal totalFuelEnergyCost;
    private BigDecimal totalMaintenanceCost;
    private BigDecimal totalInsuranceCost;
    private BigDecimal estimatedResaleValue;
    private BigDecimal netTotalCostOfOwnership;
    private BigDecimal effectiveMonthlyOwnershipCost;
    private Map<String, BigDecimal> costBreakdown = new HashMap<>();
    private String disclaimer;

    public OwnershipCostResult() {}

    public Integer getTenureYears() { return tenureYears; }
    public void setTenureYears(Integer tenureYears) { this.tenureYears = tenureYears; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getFinancingInterest() { return financingInterest; }
    public void setFinancingInterest(BigDecimal financingInterest) { this.financingInterest = financingInterest; }

    public BigDecimal getTotalFuelEnergyCost() { return totalFuelEnergyCost; }
    public void setTotalFuelEnergyCost(BigDecimal totalFuelEnergyCost) { this.totalFuelEnergyCost = totalFuelEnergyCost; }

    public BigDecimal getTotalMaintenanceCost() { return totalMaintenanceCost; }
    public void setTotalMaintenanceCost(BigDecimal totalMaintenanceCost) { this.totalMaintenanceCost = totalMaintenanceCost; }

    public BigDecimal getTotalInsuranceCost() { return totalInsuranceCost; }
    public void setTotalInsuranceCost(BigDecimal totalInsuranceCost) { this.totalInsuranceCost = totalInsuranceCost; }

    public BigDecimal getEstimatedResaleValue() { return estimatedResaleValue; }
    public void setEstimatedResaleValue(BigDecimal estimatedResaleValue) { this.estimatedResaleValue = estimatedResaleValue; }

    public BigDecimal getNetTotalCostOfOwnership() { return netTotalCostOfOwnership; }
    public void setNetTotalCostOfOwnership(BigDecimal netTotalCostOfOwnership) { this.netTotalCostOfOwnership = netTotalCostOfOwnership; }

    public BigDecimal getEffectiveMonthlyOwnershipCost() { return effectiveMonthlyOwnershipCost; }
    public void setEffectiveMonthlyOwnershipCost(BigDecimal effectiveMonthlyOwnershipCost) { this.effectiveMonthlyOwnershipCost = effectiveMonthlyOwnershipCost; }

    public Map<String, BigDecimal> getCostBreakdown() { return costBreakdown; }
    public void setCostBreakdown(Map<String, BigDecimal> costBreakdown) { this.costBreakdown = costBreakdown; }

    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
}
