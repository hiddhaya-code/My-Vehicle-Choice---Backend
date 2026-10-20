package com.myvehiclechoice.dto.admin;

import java.util.HashMap;
import java.util.Map;

public class AdminStatsDto {
    private long totalVehicles;
    private long totalVariants;
    private long totalBrands;
    private long totalDealers;
    private long totalEnquiries;
    private long pendingEnquiries;
    private Map<String, Long> countByCategory = new HashMap<>();
    private long recordsWithMissingSafetyRatings;
    private long recordsWithMissingEfficiency;

    public AdminStatsDto() {}

    public long getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(long totalVehicles) { this.totalVehicles = totalVehicles; }

    public long getTotalVariants() { return totalVariants; }
    public void setTotalVariants(long totalVariants) { this.totalVariants = totalVariants; }

    public long getTotalBrands() { return totalBrands; }
    public void setTotalBrands(long totalBrands) { this.totalBrands = totalBrands; }

    public long getTotalDealers() { return totalDealers; }
    public void setTotalDealers(long totalDealers) { this.totalDealers = totalDealers; }

    public long getTotalEnquiries() { return totalEnquiries; }
    public void setTotalEnquiries(long totalEnquiries) { this.totalEnquiries = totalEnquiries; }

    public long getPendingEnquiries() { return pendingEnquiries; }
    public void setPendingEnquiries(long pendingEnquiries) { this.pendingEnquiries = pendingEnquiries; }

    public Map<String, Long> getCountByCategory() { return countByCategory; }
    public void setCountByCategory(Map<String, Long> countByCategory) { this.countByCategory = countByCategory; }

    public long getRecordsWithMissingSafetyRatings() { return recordsWithMissingSafetyRatings; }
    public void setRecordsWithMissingSafetyRatings(long recordsWithMissingSafetyRatings) { this.recordsWithMissingSafetyRatings = recordsWithMissingSafetyRatings; }

    public long getRecordsWithMissingEfficiency() { return recordsWithMissingEfficiency; }
    public void setRecordsWithMissingEfficiency(long recordsWithMissingEfficiency) { this.recordsWithMissingEfficiency = recordsWithMissingEfficiency; }
}
