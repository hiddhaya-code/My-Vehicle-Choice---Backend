package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.calculator.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public EmiCalculationResult calculateEmi(EmiCalculationRequest request) {
        if (request.getDownPayment().compareTo(request.getVehiclePrice()) > 0) {
            throw new IllegalArgumentException("Down payment cannot be greater than the total vehicle price.");
        }

        BigDecimal principal = request.getVehiclePrice().subtract(request.getDownPayment());
        double annualRate = request.getAnnualInterestRate();
        int tenureMonths = request.getTenureMonths();

        BigDecimal monthlyEmi;
        if (annualRate == 0 || principal.compareTo(BigDecimal.ZERO) == 0) {
            monthlyEmi = principal.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP);
        } else {
            double monthlyRate = annualRate / (12.0 * 100.0);
            double numerator = principal.doubleValue() * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths);
            double denominator = Math.pow(1 + monthlyRate, tenureMonths) - 1;
            double emiDouble = numerator / denominator;
            monthlyEmi = BigDecimal.valueOf(emiDouble).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal totalRepayment = monthlyEmi.multiply(BigDecimal.valueOf(tenureMonths)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalInterest = totalRepayment.subtract(principal).setScale(2, RoundingMode.HALF_UP);

        EmiCalculationResult result = new EmiCalculationResult();
        result.setLoanPrincipal(principal);
        result.setMonthlyEmi(monthlyEmi);
        result.setTotalRepayment(totalRepayment);
        result.setTotalInterest(totalInterest);
        result.setAnnualInterestRate(annualRate);
        result.setTenureMonths(tenureMonths);
        result.setAssumptions(String.format("Calculated using standard reducing-balance method with %.2f%% p.a. interest over %d months.", annualRate, tenureMonths));

        return result;
    }

    public RunningCostResult calculateRunningCost(RunningCostRequest request) {
        double monthlyKm = request.getMonthlyDistanceKm() != null ? request.getMonthlyDistanceKm() : 1000.0;
        boolean isEv = Boolean.TRUE.equals(request.getIsElectric());

        RunningCostResult result = new RunningCostResult();

        if (isEv) {
            // EV Calculation
            double batteryKwh = request.getEvBatteryCapacityKwh() != null ? request.getEvBatteryCapacityKwh() : 45.0;
            double rangeKm = request.getEvCertifiedRangeKm() != null ? request.getEvCertifiedRangeKm() : 400.0;
            double powerRate = request.getElectricityCostPerKwh() != null ? request.getElectricityCostPerKwh() : 8.50;

            double energyPerKm = batteryKwh / rangeKm;
            double costPerKm = energyPerKm * powerRate;
            double monthlyCost = monthlyKm * costPerKm;

            result.setCostPerKm(BigDecimal.valueOf(costPerKm).setScale(2, RoundingMode.HALF_UP));
            result.setMonthlyCost(BigDecimal.valueOf(monthlyCost).setScale(2, RoundingMode.HALF_UP));
            result.setAnnualCost(BigDecimal.valueOf(monthlyCost * 12).setScale(2, RoundingMode.HALF_UP));

            // Equivalent petrol benchmark (assuming ~17 kmpl at ₹96.72/L)
            double benchmarkFuelCost = (monthlyKm / 17.0) * 96.72;
            result.setEquivalentPetrolCostMonthly(BigDecimal.valueOf(benchmarkFuelCost).setScale(2, RoundingMode.HALF_UP));
            result.setProjectedAnnualSavings(BigDecimal.valueOf((benchmarkFuelCost - monthlyCost) * 12).setScale(2, RoundingMode.HALF_UP));
            result.setPowertrainType("ELECTRIC");
            result.setFormulaExplanation(String.format(
                    "Energy consumed: %.3f kWh/km based on %.1f kWh battery pack and %d km certified range at ₹%.2f/kWh domestic power tariff.",
                    energyPerKm, batteryKwh, (int) rangeKm, powerRate
            ));
        } else {
            // Petrol / ICE Calculation
            double mileage = request.getVehicleEfficiency() != null && request.getVehicleEfficiency() > 0 ? request.getVehicleEfficiency() : 18.0;
            double fuelPrice = request.getFuelPricePerUnit() != null ? request.getFuelPricePerUnit() : 96.72;

            double litresPerMonth = monthlyKm / mileage;
            double monthlyCost = litresPerMonth * fuelPrice;
            double costPerKm = fuelPrice / mileage;

            result.setCostPerKm(BigDecimal.valueOf(costPerKm).setScale(2, RoundingMode.HALF_UP));
            result.setMonthlyCost(BigDecimal.valueOf(monthlyCost).setScale(2, RoundingMode.HALF_UP));
            result.setAnnualCost(BigDecimal.valueOf(monthlyCost * 12).setScale(2, RoundingMode.HALF_UP));
            result.setEquivalentPetrolCostMonthly(result.getMonthlyCost());
            result.setProjectedAnnualSavings(BigDecimal.ZERO);
            result.setPowertrainType("INTERNAL_COMBUSTION");
            result.setFormulaExplanation(String.format(
                    "Fuel consumed: %.1f Litres/month at %.2f km/l fuel economy and ₹%.2f/L fuel retail price.",
                    litresPerMonth, mileage, fuelPrice
            ));
        }

        return result;
    }

    public OwnershipCostResult calculateOwnershipCost(BigDecimal purchasePrice, int years, double annualKm, boolean isElectric, Double mileageKmpl) {
        years = (years == 3 || years == 5) ? years : 5;
        double totalKm = annualKm * years;

        // 1. Purchase & Loan Interest (assuming 80% loan @ 9.5% for 5 years)
        BigDecimal downPayment = purchasePrice.multiply(new BigDecimal("0.20"));
        EmiCalculationRequest emiReq = new EmiCalculationRequest();
        emiReq.setVehiclePrice(purchasePrice);
        emiReq.setDownPayment(downPayment);
        emiReq.setAnnualInterestRate(9.5);
        emiReq.setTenureMonths(years * 12);
        EmiCalculationResult emiRes = calculateEmi(emiReq);

        BigDecimal financingInterest = emiRes.getTotalInterest();

        // 2. Fuel / Energy cost over tenure
        double costPerKm = isElectric ? 1.15 : (96.72 / (mileageKmpl != null ? mileageKmpl : 17.5));
        BigDecimal totalFuelCost = BigDecimal.valueOf(totalKm * costPerKm).setScale(2, RoundingMode.HALF_UP);

        // 3. Maintenance Cost
        double annualMaint = isElectric ? 4500.0 : 8000.0;
        BigDecimal totalMaintCost = BigDecimal.valueOf(annualMaint * years).setScale(2, RoundingMode.HALF_UP);

        // 4. Insurance renewals (approx 3% of depreciated value per year)
        double totalInsurance = purchasePrice.doubleValue() * 0.035 * years * 0.8;
        BigDecimal insuranceCost = BigDecimal.valueOf(totalInsurance).setScale(2, RoundingMode.HALF_UP);

        // 5. Estimated Resale Value (Depreciation: ~40% drop after 3 yrs, ~55% drop after 5 yrs)
        double retentionRate = years == 3 ? 0.60 : 0.45;
        BigDecimal resaleValue = purchasePrice.multiply(BigDecimal.valueOf(retentionRate)).setScale(2, RoundingMode.HALF_UP);

        // Net TCO = Purchase + Interest + Fuel + Maintenance + Insurance - Resale Value
        BigDecimal grossCost = purchasePrice.add(financingInterest).add(totalFuelCost).add(totalMaintCost).add(insuranceCost);
        BigDecimal netTco = grossCost.subtract(resaleValue).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyEffective = netTco.divide(BigDecimal.valueOf(years * 12), 2, RoundingMode.HALF_UP);

        OwnershipCostResult res = new OwnershipCostResult();
        res.setTenureYears(years);
        res.setPurchasePrice(purchasePrice);
        res.setFinancingInterest(financingInterest);
        res.setTotalFuelEnergyCost(totalFuelCost);
        res.setTotalMaintenanceCost(totalMaintCost);
        res.setTotalInsuranceCost(insuranceCost);
        res.setEstimatedResaleValue(resaleValue);
        res.setNetTotalCostOfOwnership(netTco);
        res.setEffectiveMonthlyOwnershipCost(monthlyEffective);

        res.getCostBreakdown().put("Purchase Price", purchasePrice);
        res.getCostBreakdown().put("Financing Interest", financingInterest);
        res.getCostBreakdown().put("Fuel / Electricity", totalFuelCost);
        res.getCostBreakdown().put("Scheduled Maintenance", totalMaintCost);
        res.getCostBreakdown().put("Insurance Renewals", insuranceCost);
        res.getCostBreakdown().put("Estimated Resale Value (Credit)", resaleValue.negate());

        res.setDisclaimer(String.format(
                "Estimated %d-year total cost of ownership assumes %d km total travel, 9.5%% p.a. vehicle loan, and standard Indian depreciation curves.",
                years, (int) totalKm
        ));

        return res;
    }
}
