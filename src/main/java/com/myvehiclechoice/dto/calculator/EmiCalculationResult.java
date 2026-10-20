package com.myvehiclechoice.dto.calculator;

import java.math.BigDecimal;

public class EmiCalculationResult {
    private BigDecimal loanPrincipal;
    private BigDecimal monthlyEmi;
    private BigDecimal totalRepayment;
    private BigDecimal totalInterest;
    private Double annualInterestRate;
    private Integer tenureMonths;
    private String assumptions;

    public EmiCalculationResult() {}

    public BigDecimal getLoanPrincipal() { return loanPrincipal; }
    public void setLoanPrincipal(BigDecimal loanPrincipal) { this.loanPrincipal = loanPrincipal; }

    public BigDecimal getMonthlyEmi() { return monthlyEmi; }
    public void setMonthlyEmi(BigDecimal monthlyEmi) { this.monthlyEmi = monthlyEmi; }

    public BigDecimal getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(BigDecimal totalRepayment) { this.totalRepayment = totalRepayment; }

    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }

    public Double getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(Double annualInterestRate) { this.annualInterestRate = annualInterestRate; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

    public String getAssumptions() { return assumptions; }
    public void setAssumptions(String assumptions) { this.assumptions = assumptions; }
}
