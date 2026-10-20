package com.myvehiclechoice.dto.recommendation;

import com.myvehiclechoice.dto.VehicleSummaryDto;
import com.myvehiclechoice.dto.VehicleVariantDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationResultDto {
    private int rank;
    private VehicleSummaryDto vehicle;
    private VehicleVariantDto recommendedVariant;
    private double overallScore; // Out of 100
    private double dataConfidenceScore; // 0 to 100% based on verified OEM attributes
    private String dataConfidenceLevel; // HIGH_VERIFIED, MEDIUM_VERIFIED, CAUTION_MISSING_DATA
    private Map<String, Double> scoreBreakdown = new HashMap<>();
    private String matchBadge; // "Best Overall Match", "Best for Mileage", "Best for Safety", "Best Budget Option"
    private List<String> reasons = new ArrayList<>(); // 3 personalized reasons
    private List<String> tradeOffs = new ArrayList<>(); // Honest compromises / limitations
    private Map<String, String> categorySpecs = new HashMap<>(); // Category-specific technical specs
    private OwnershipAffordabilityDto affordability; // Detailed 5-yr TCO, EMI & energy cost
    private String relativePositionExplanation; // Explain why ranked higher/lower
    private List<String> missingDataWarnings = new ArrayList<>();
    private boolean isNearMatch = false;

    public RecommendationResultDto() {}

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public VehicleSummaryDto getVehicle() { return vehicle; }
    public void setVehicle(VehicleSummaryDto vehicle) { this.vehicle = vehicle; }

    public VehicleVariantDto getRecommendedVariant() { return recommendedVariant; }
    public void setRecommendedVariant(VehicleVariantDto recommendedVariant) { this.recommendedVariant = recommendedVariant; }

    public double getOverallScore() { return overallScore; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }

    public double getDataConfidenceScore() { return dataConfidenceScore; }
    public void setDataConfidenceScore(double dataConfidenceScore) { this.dataConfidenceScore = dataConfidenceScore; }

    public String getDataConfidenceLevel() { return dataConfidenceLevel; }
    public void setDataConfidenceLevel(String dataConfidenceLevel) { this.dataConfidenceLevel = dataConfidenceLevel; }

    public Map<String, Double> getScoreBreakdown() { return scoreBreakdown; }
    public void setScoreBreakdown(Map<String, Double> scoreBreakdown) { this.scoreBreakdown = scoreBreakdown; }

    public String getMatchBadge() { return matchBadge; }
    public void setMatchBadge(String matchBadge) { this.matchBadge = matchBadge; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    public List<String> getTradeOffs() { return tradeOffs; }
    public void setTradeOffs(List<String> tradeOffs) { this.tradeOffs = tradeOffs; }

    public Map<String, String> getCategorySpecs() { return categorySpecs; }
    public void setCategorySpecs(Map<String, String> categorySpecs) { this.categorySpecs = categorySpecs; }

    public OwnershipAffordabilityDto getAffordability() { return affordability; }
    public void setAffordability(OwnershipAffordabilityDto affordability) { this.affordability = affordability; }

    public String getRelativePositionExplanation() { return relativePositionExplanation; }
    public void setRelativePositionExplanation(String relativePositionExplanation) { this.relativePositionExplanation = relativePositionExplanation; }

    public List<String> getMissingDataWarnings() { return missingDataWarnings; }
    public void setMissingDataWarnings(List<String> missingDataWarnings) { this.missingDataWarnings = missingDataWarnings; }

    public boolean isNearMatch() { return isNearMatch; }
    public void setNearMatch(boolean nearMatch) { isNearMatch = nearMatch; }
}
