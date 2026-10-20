package com.myvehiclechoice.dto.calculator;

public class RunningCostRequest {
    private Double monthlyDistanceKm = 1000.0;
    private Double fuelPricePerUnit = 96.72; // Default petrol price in Delhi
    private Double vehicleEfficiency = 18.0; // kmpl or km/kWh
    private Boolean isElectric = false;
    private Double electricityCostPerKwh = 8.50; // Domestic electricity slab
    private Double evBatteryCapacityKwh = 45.0;
    private Double evCertifiedRangeKm = 465.0;

    public RunningCostRequest() {}

    public Double getMonthlyDistanceKm() { return monthlyDistanceKm; }
    public void setMonthlyDistanceKm(Double monthlyDistanceKm) { this.monthlyDistanceKm = monthlyDistanceKm; }

    public Double getFuelPricePerUnit() { return fuelPricePerUnit; }
    public void setFuelPricePerUnit(Double fuelPricePerUnit) { this.fuelPricePerUnit = fuelPricePerUnit; }

    public Double getVehicleEfficiency() { return vehicleEfficiency; }
    public void setVehicleEfficiency(Double vehicleEfficiency) { this.vehicleEfficiency = vehicleEfficiency; }

    public Boolean getIsElectric() { return isElectric; }
    public void setIsElectric(Boolean isElectric) { this.isElectric = isElectric; }

    public Double getElectricityCostPerKwh() { return electricityCostPerKwh; }
    public void setElectricityCostPerKwh(Double electricityCostPerKwh) { this.electricityCostPerKwh = electricityCostPerKwh; }

    public Double getEvBatteryCapacityKwh() { return evBatteryCapacityKwh; }
    public void setEvBatteryCapacityKwh(Double evBatteryCapacityKwh) { this.evBatteryCapacityKwh = evBatteryCapacityKwh; }

    public Double getEvCertifiedRangeKm() { return evCertifiedRangeKm; }
    public void setEvCertifiedRangeKm(Double evCertifiedRangeKm) { this.evCertifiedRangeKm = evCertifiedRangeKm; }
}
