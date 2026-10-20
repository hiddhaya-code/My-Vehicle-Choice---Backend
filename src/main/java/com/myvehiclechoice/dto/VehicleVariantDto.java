package com.myvehiclechoice.dto;

import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VehicleVariantDto {
    private Long id;
    private Long vehicleId;
    private String variantName;
    private Integer modelYear;
    private FuelType fuelType;
    private TransmissionType transmission;
    private BigDecimal exShowroomPrice;
    private BigDecimal estimatedOnRoadPrice;
    private String cityPriceRef;
    private BigDecimal mileageKmpl;
    private Integer electricRangeKm;
    private BigDecimal batteryCapacityKwh;
    private Integer engineCc;
    private BigDecimal maxPowerBhp;
    private Integer maxPowerRpm;
    private BigDecimal maxTorqueNm;
    private Integer seatingCapacity;
    private Integer bootSpaceLitres;
    private Integer groundClearanceMm;
    private BigDecimal fuelTankCapacityLitres;
    private Integer kerbWeightKg;
    private Integer seatHeightMm;
    private Integer safetyRatingStars;
    private String safetyRatingProtocol;
    private Integer airbagsCount;
    private Boolean hasAbs;
    private Boolean hasEsp;
    private BigDecimal maintenanceEstimateAnnual;
    private String keyAdvantages;
    private String keyDisadvantages;
    private Boolean isVerified;
    private String dataSource;
    private LocalDate verifiedDate;

    public VehicleVariantDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }

    public Integer getModelYear() { return modelYear; }
    public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    public TransmissionType getTransmission() { return transmission; }
    public void setTransmission(TransmissionType transmission) { this.transmission = transmission; }

    public BigDecimal getExShowroomPrice() { return exShowroomPrice; }
    public void setExShowroomPrice(BigDecimal exShowroomPrice) { this.exShowroomPrice = exShowroomPrice; }

    public BigDecimal getEstimatedOnRoadPrice() { return estimatedOnRoadPrice; }
    public void setEstimatedOnRoadPrice(BigDecimal estimatedOnRoadPrice) { this.estimatedOnRoadPrice = estimatedOnRoadPrice; }

    public String getCityPriceRef() { return cityPriceRef; }
    public void setCityPriceRef(String cityPriceRef) { this.cityPriceRef = cityPriceRef; }

    public BigDecimal getMileageKmpl() { return mileageKmpl; }
    public void setMileageKmpl(BigDecimal mileageKmpl) { this.mileageKmpl = mileageKmpl; }

    public Integer getElectricRangeKm() { return electricRangeKm; }
    public void setElectricRangeKm(Integer electricRangeKm) { this.electricRangeKm = electricRangeKm; }

    public BigDecimal getBatteryCapacityKwh() { return batteryCapacityKwh; }
    public void setBatteryCapacityKwh(BigDecimal batteryCapacityKwh) { this.batteryCapacityKwh = batteryCapacityKwh; }

    public Integer getEngineCc() { return engineCc; }
    public void setEngineCc(Integer engineCc) { this.engineCc = engineCc; }

    public BigDecimal getMaxPowerBhp() { return maxPowerBhp; }
    public void setMaxPowerBhp(BigDecimal maxPowerBhp) { this.maxPowerBhp = maxPowerBhp; }

    public Integer getMaxPowerRpm() { return maxPowerRpm; }
    public void setMaxPowerRpm(Integer maxPowerRpm) { this.maxPowerRpm = maxPowerRpm; }

    public BigDecimal getMaxTorqueNm() { return maxTorqueNm; }
    public void setMaxTorqueNm(BigDecimal maxTorqueNm) { this.maxTorqueNm = maxTorqueNm; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public Integer getBootSpaceLitres() { return bootSpaceLitres; }
    public void setBootSpaceLitres(Integer bootSpaceLitres) { this.bootSpaceLitres = bootSpaceLitres; }

    public Integer getGroundClearanceMm() { return groundClearanceMm; }
    public void setGroundClearanceMm(Integer groundClearanceMm) { this.groundClearanceMm = groundClearanceMm; }

    public BigDecimal getFuelTankCapacityLitres() { return fuelTankCapacityLitres; }
    public void setFuelTankCapacityLitres(BigDecimal fuelTankCapacityLitres) { this.fuelTankCapacityLitres = fuelTankCapacityLitres; }

    public Integer getKerbWeightKg() { return kerbWeightKg; }
    public void setKerbWeightKg(Integer kerbWeightKg) { this.kerbWeightKg = kerbWeightKg; }

    public Integer getSeatHeightMm() { return seatHeightMm; }
    public void setSeatHeightMm(Integer seatHeightMm) { this.seatHeightMm = seatHeightMm; }

    public Integer getSafetyRatingStars() { return safetyRatingStars; }
    public void setSafetyRatingStars(Integer safetyRatingStars) { this.safetyRatingStars = safetyRatingStars; }

    public String getSafetyRatingProtocol() { return safetyRatingProtocol; }
    public void setSafetyRatingProtocol(String safetyRatingProtocol) { this.safetyRatingProtocol = safetyRatingProtocol; }

    public Integer getAirbagsCount() { return airbagsCount; }
    public void setAirbagsCount(Integer airbagsCount) { this.airbagsCount = airbagsCount; }

    public Boolean getHasAbs() { return hasAbs; }
    public void setHasAbs(Boolean hasAbs) { this.hasAbs = hasAbs; }

    public Boolean getHasEsp() { return hasEsp; }
    public void setHasEsp(Boolean hasEsp) { this.hasEsp = hasEsp; }

    public BigDecimal getMaintenanceEstimateAnnual() { return maintenanceEstimateAnnual; }
    public void setMaintenanceEstimateAnnual(BigDecimal maintenanceEstimateAnnual) { this.maintenanceEstimateAnnual = maintenanceEstimateAnnual; }

    public String getKeyAdvantages() { return keyAdvantages; }
    public void setKeyAdvantages(String keyAdvantages) { this.keyAdvantages = keyAdvantages; }

    public String getKeyDisadvantages() { return keyDisadvantages; }
    public void setKeyDisadvantages(String keyDisadvantages) { this.keyDisadvantages = keyDisadvantages; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public LocalDate getVerifiedDate() { return verifiedDate; }
    public void setVerifiedDate(LocalDate verifiedDate) { this.verifiedDate = verifiedDate; }
}
