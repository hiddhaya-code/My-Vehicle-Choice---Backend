package com.myvehiclechoice.dto.calculator;

import java.math.BigDecimal;

public class RunningCostResult {
    private BigDecimal monthlyCost;
    private BigDecimal annualCost;
    private BigDecimal costPerKm;
    private BigDecimal equivalentPetrolCostMonthly;
    private BigDecimal projectedAnnualSavings;
    private String powertrainType;
    private String formulaExplanation;

    public RunningCostResult() {}

    public BigDecimal getMonthlyCost() { return monthlyCost; }
    public void setMonthlyCost(BigDecimal monthlyCost) { this.monthlyCost = monthlyCost; }

    public BigDecimal getAnnualCost() { return annualCost; }
    public void setAnnualCost(BigDecimal annualCost) { this.annualCost = annualCost; }

    public BigDecimal getCostPerKm() { return costPerKm; }
    public void setCostPerKm(BigDecimal costPerKm) { this.costPerKm = costPerKm; }

    public BigDecimal getEquivalentPetrolCostMonthly() { return equivalentPetrolCostMonthly; }
    public void setEquivalentPetrolCostMonthly(BigDecimal equivalentPetrolCostMonthly) { this.equivalentPetrolCostMonthly = equivalentPetrolCostMonthly; }

    public BigDecimal getProjectedAnnualSavings() { return projectedAnnualSavings; }
    public void setProjectedAnnualSavings(BigDecimal projectedAnnualSavings) { this.projectedAnnualSavings = projectedAnnualSavings; }

    public String getPowertrainType() { return powertrainType; }
    public void setPowertrainType(String powertrainType) { this.powertrainType = powertrainType; }

    public String getFormulaExplanation() { return formulaExplanation; }
    public void setFormulaExplanation(String formulaExplanation) { this.formulaExplanation = formulaExplanation; }
}
