package com.myvehiclechoice.dto.recommendation;

import com.myvehiclechoice.model.enums.VehicleCategory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireAnswersDto {
    private BigDecimal budgetAmount;
    private String budgetType = "PURCHASE"; // PURCHASE or EMI
    private Boolean isStrictBudget = true; // If true, strictly reject out-of-budget vehicles
    private String state = "Delhi";
    private String city = "New Delhi";
    private VehicleCategory category; // CAR, MOTORCYCLE, SCOOTER, ELECTRIC_CAR, ELECTRIC_TWO_WHEELER
    private String bodyStyle; // SUV, SEDAN, HATCHBACK, SPORT, COMMUTER, CRUISER, SCOOTER
    private List<String> preferredFuels = new ArrayList<>(); // PETROL, DIESEL, ELECTRIC, HYBRID, CNG
    private String preferredTransmission = "ANY"; // MANUAL, AUTOMATIC, ANY
    private Integer dailyDistanceKm = 35;
    private Integer cityDrivingPercent = 75;
    private Boolean evHomeChargingAvailable = true;
    private String evChargingAccess = "HOME_GARAGE"; // HOME_GARAGE, APARTMENT_SOCKET, PUBLIC_ONLY, NONE
    private String performancePriority = "BALANCED"; // RELAXED, BALANCED, SPORTY, HIGH_PERFORMANCE
    private Integer minSeatingCapacity;
    private Boolean highGroundClearanceNeeded = false;
    private String ridingErgonomics = "COMMUTER_ACCESSIBLE"; // UPRIGHT_RELAXED, COMMUTER_ACCESSIBLE, SPORTY_COMMITTED
    private List<String> topPriorities = new ArrayList<>(); // SAFETY, MILEAGE, MAINTENANCE, COMFORT, PERFORMANCE, RELIABILITY
    private List<String> nonNegotiables = new ArrayList<>(); // e.g. "SAFETY_5_STAR", "AUTOMATIC", "STRICT_BUDGET", "ZERO_EMISSIONS", "DUAL_ABS"
    private Boolean allowNearMatches = false;

    public QuestionnaireAnswersDto() {}

    // Getters and Setters
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public String getBudgetType() { return budgetType; }
    public void setBudgetType(String budgetType) { this.budgetType = budgetType; }

    public Boolean getIsStrictBudget() { return isStrictBudget; }
    public void setIsStrictBudget(Boolean isStrictBudget) { this.isStrictBudget = isStrictBudget; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public VehicleCategory getCategory() { return category; }
    public void setCategory(VehicleCategory category) { this.category = category; }

    public String getBodyStyle() { return bodyStyle; }
    public void setBodyStyle(String bodyStyle) { this.bodyStyle = bodyStyle; }

    public List<String> getPreferredFuels() { return preferredFuels; }
    public void setPreferredFuels(List<String> preferredFuels) { this.preferredFuels = preferredFuels; }

    public String getPreferredTransmission() { return preferredTransmission; }
    public void setPreferredTransmission(String preferredTransmission) { this.preferredTransmission = preferredTransmission; }

    public Integer getDailyDistanceKm() { return dailyDistanceKm; }
    public void setDailyDistanceKm(Integer dailyDistanceKm) { this.dailyDistanceKm = dailyDistanceKm; }

    public Integer getCityDrivingPercent() { return cityDrivingPercent; }
    public void setCityDrivingPercent(Integer cityDrivingPercent) { this.cityDrivingPercent = cityDrivingPercent; }

    public Boolean getEvHomeChargingAvailable() { return evHomeChargingAvailable; }
    public void setEvHomeChargingAvailable(Boolean evHomeChargingAvailable) { this.evHomeChargingAvailable = evHomeChargingAvailable; }

    public String getEvChargingAccess() { return evChargingAccess; }
    public void setEvChargingAccess(String evChargingAccess) { this.evChargingAccess = evChargingAccess; }

    public String getPerformancePriority() { return performancePriority; }
    public void setPerformancePriority(String performancePriority) { this.performancePriority = performancePriority; }

    public Integer getMinSeatingCapacity() { return minSeatingCapacity; }
    public void setMinSeatingCapacity(Integer minSeatingCapacity) { this.minSeatingCapacity = minSeatingCapacity; }

    public Boolean getHighGroundClearanceNeeded() { return highGroundClearanceNeeded; }
    public void setHighGroundClearanceNeeded(Boolean highGroundClearanceNeeded) { this.highGroundClearanceNeeded = highGroundClearanceNeeded; }

    public String getRidingErgonomics() { return ridingErgonomics; }
    public void setRidingErgonomics(String ridingErgonomics) { this.ridingErgonomics = ridingErgonomics; }

    public List<String> getTopPriorities() { return topPriorities; }
    public void setTopPriorities(List<String> topPriorities) { this.topPriorities = topPriorities; }

    public List<String> getNonNegotiables() { return nonNegotiables; }
    public void setNonNegotiables(List<String> nonNegotiables) { this.nonNegotiables = nonNegotiables; }

    public Boolean getAllowNearMatches() { return allowNearMatches; }
    public void setAllowNearMatches(Boolean allowNearMatches) { this.allowNearMatches = allowNearMatches; }
}
