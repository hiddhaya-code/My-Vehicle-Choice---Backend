package com.myvehiclechoice.model.entity;

import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_variants", uniqueConstraints = {
    @UniqueConstraint(name = "uk_vehicle_variant_year", columnNames = {"vehicle_id", "variant_name", "model_year"})
})
public class VehicleVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "variant_name", nullable = false, length = 150)
    private String variantName;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 50)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TransmissionType transmission;

    @Column(name = "ex_showroom_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal exShowroomPrice;

    @Column(name = "estimated_on_road_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedOnRoadPrice;

    @Column(name = "city_price_ref", length = 50)
    private String cityPriceRef = "New Delhi";

    @Column(name = "mileage_kmpl", precision = 5, scale = 2)
    private BigDecimal mileageKmpl;

    @Column(name = "electric_range_km")
    private Integer electricRangeKm;

    @Column(name = "battery_capacity_kwh", precision = 6, scale = 2)
    private BigDecimal batteryCapacityKwh;

    @Column(name = "engine_cc")
    private Integer engineCc;

    @Column(name = "max_power_bhp", precision = 6, scale = 2)
    private BigDecimal maxPowerBhp;

    @Column(name = "max_power_rpm")
    private Integer maxPowerRpm;

    @Column(name = "max_torque_nm", precision = 6, scale = 2)
    private BigDecimal maxTorqueNm;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @Column(name = "boot_space_litres")
    private Integer bootSpaceLitres;

    @Column(name = "ground_clearance_mm")
    private Integer groundClearanceMm;

    @Column(name = "fuel_tank_capacity_litres", precision = 5, scale = 2)
    private BigDecimal fuelTankCapacityLitres;

    @Column(name = "kerb_weight_kg")
    private Integer kerbWeightKg;

    @Column(name = "seat_height_mm")
    private Integer seatHeightMm;

    @Column(name = "safety_rating_stars")
    private Integer safetyRatingStars;

    @Column(name = "safety_rating_protocol", length = 50)
    private String safetyRatingProtocol;

    @Column(name = "airbags_count")
    private Integer airbagsCount;

    @Column(name = "has_abs")
    private Boolean hasAbs = true;

    @Column(name = "has_esp")
    private Boolean hasEsp = false;

    @Column(name = "maintenance_estimate_annual", precision = 10, scale = 2)
    private BigDecimal maintenanceEstimateAnnual;

    @Column(name = "key_advantages", columnDefinition = "TEXT")
    private String keyAdvantages;

    @Column(name = "key_disadvantages", columnDefinition = "TEXT")
    private String keyDisadvantages;

    @Column(name = "is_verified")
    private Boolean isVerified = true;

    @Column(name = "data_source", length = 150)
    private String dataSource;

    @Column(name = "verified_date")
    private LocalDate verifiedDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VehicleVariant() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
