package com.myvehiclechoice;

import com.myvehiclechoice.dto.calculator.EmiCalculationRequest;
import com.myvehiclechoice.dto.calculator.EmiCalculationResult;
import com.myvehiclechoice.dto.calculator.RunningCostRequest;
import com.myvehiclechoice.dto.calculator.RunningCostResult;
import com.myvehiclechoice.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    @DisplayName("Should calculate monthly EMI accurately with reducing-balance formula")
    void testStandardEmiCalculation() {
        EmiCalculationRequest req = new EmiCalculationRequest();
        req.setVehiclePrice(new BigDecimal("1000000.00")); // ₹10 Lakh
        req.setDownPayment(new BigDecimal("200000.00"));   // ₹2 Lakh down payment
        req.setAnnualInterestRate(9.5);                    // 9.5% p.a.
        req.setTenureMonths(60);                           // 5 Years

        EmiCalculationResult result = calculatorService.calculateEmi(req);

        assertNotNull(result);
        assertEquals(new BigDecimal("800000.00"), result.getLoanPrincipal());
        // For 800,000 @ 9.5% for 60 months, monthly EMI is ~16,796
        assertTrue(result.getMonthlyEmi().doubleValue() > 16500 && result.getMonthlyEmi().doubleValue() < 17000);
        assertTrue(result.getTotalInterest().doubleValue() > 200000);
    }

    @Test
    @DisplayName("Should handle 0% promotional interest loan without division by zero")
    void testZeroInterestLoan() {
        EmiCalculationRequest req = new EmiCalculationRequest();
        req.setVehiclePrice(new BigDecimal("600000.00"));
        req.setDownPayment(new BigDecimal("120000.00"));
        req.setAnnualInterestRate(0.0);
        req.setTenureMonths(48);

        EmiCalculationResult result = calculatorService.calculateEmi(req);

        assertEquals(new BigDecimal("480000.00"), result.getLoanPrincipal());
        assertEquals(new BigDecimal("10000.00"), result.getMonthlyEmi());
        assertEquals(BigDecimal.ZERO.setScale(2), result.getTotalInterest());
    }

    @Test
    @DisplayName("Should throw exception if down payment exceeds vehicle purchase price")
    void testInvalidDownPayment() {
        EmiCalculationRequest req = new EmiCalculationRequest();
        req.setVehiclePrice(new BigDecimal("500000.00"));
        req.setDownPayment(new BigDecimal("600000.00")); // Greater than price
        req.setAnnualInterestRate(9.0);
        req.setTenureMonths(36);

        assertThrows(IllegalArgumentException.class, () -> calculatorService.calculateEmi(req));
    }

    @Test
    @DisplayName("Should calculate EV running costs and project annual fuel savings vs petrol")
    void testEvRunningCostSavings() {
        RunningCostRequest req = new RunningCostRequest();
        req.setIsElectric(true);
        req.setMonthlyDistanceKm(1500.0);
        req.setEvBatteryCapacityKwh(45.0);
        req.setEvCertifiedRangeKm(465.0);
        req.setElectricityCostPerKwh(8.50);

        RunningCostResult result = calculatorService.calculateRunningCost(req);

        assertNotNull(result);
        assertEquals("ELECTRIC", result.getPowertrainType());
        // EV running cost should be under ~₹1.20/km
        assertTrue(result.getCostPerKm().doubleValue() < 1.20);
        // Annual savings vs petrol should be substantial (> ₹70,000)
        assertTrue(result.getProjectedAnnualSavings().doubleValue() > 70000);
    }
}
