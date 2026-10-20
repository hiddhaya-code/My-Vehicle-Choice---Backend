package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.calculator.*;
import com.myvehiclechoice.service.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calculators")
@Tag(name = "Financial & Ownership Calculators", description = "EMI, Running Cost, and Total Cost of Ownership (TCO) APIs")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/emi")
    @Operation(summary = "Calculate monthly loan EMI and total interest using standard reducing-balance formula")
    public ResponseEntity<ApiResponse<EmiCalculationResult>> calculateEmi(@Valid @RequestBody EmiCalculationRequest request) {
        EmiCalculationResult result = calculatorService.calculateEmi(request);
        return ResponseEntity.ok(ApiResponse.ok("EMI calculation successful", result));
    }

    @PostMapping("/running-cost")
    @Operation(summary = "Calculate monthly and annual running expenses for Petrol vs EV")
    public ResponseEntity<ApiResponse<RunningCostResult>> calculateRunningCost(@RequestBody RunningCostRequest request) {
        RunningCostResult result = calculatorService.calculateRunningCost(request);
        return ResponseEntity.ok(ApiResponse.ok("Running cost calculation successful", result));
    }

    @PostMapping("/ownership-cost")
    @Operation(summary = "Calculate 3-year or 5-year Total Cost of Ownership (TCO)")
    public ResponseEntity<ApiResponse<OwnershipCostResult>> calculateOwnershipCost(@RequestBody Map<String, Object> payload) {
        BigDecimal purchasePrice = new BigDecimal(payload.getOrDefault("purchasePrice", "1200000").toString());
        int years = Integer.parseInt(payload.getOrDefault("years", "5").toString());
        double annualKm = Double.parseDouble(payload.getOrDefault("annualKm", "12000").toString());
        boolean isElectric = Boolean.parseBoolean(payload.getOrDefault("isElectric", "false").toString());
        Double mileage = payload.get("mileage") != null ? Double.parseDouble(payload.get("mileage").toString()) : null;

        OwnershipCostResult result = calculatorService.calculateOwnershipCost(purchasePrice, years, annualKm, isElectric, mileage);
        return ResponseEntity.ok(ApiResponse.ok("Ownership cost calculation successful", result));
    }
}
