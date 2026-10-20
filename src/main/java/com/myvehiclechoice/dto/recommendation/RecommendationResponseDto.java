package com.myvehiclechoice.dto.recommendation;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResponseDto {
    private List<RecommendationResultDto> matchedResults = new ArrayList<>();
    private List<RecommendationResultDto> nearMatchResults = new ArrayList<>();
    private List<String> unmatchedConstraintNotes = new ArrayList<>();
    private String userSummary;

    public RecommendationResponseDto() {}

    public List<RecommendationResultDto> getMatchedResults() { return matchedResults; }
    public void setMatchedResults(List<RecommendationResultDto> matchedResults) { this.matchedResults = matchedResults; }

    public List<RecommendationResultDto> getNearMatchResults() { return nearMatchResults; }
    public void setNearMatchResults(List<RecommendationResultDto> nearMatchResults) { this.nearMatchResults = nearMatchResults; }

    public List<String> getUnmatchedConstraintNotes() { return unmatchedConstraintNotes; }
    public void setUnmatchedConstraintNotes(List<String> unmatchedConstraintNotes) { this.unmatchedConstraintNotes = unmatchedConstraintNotes; }

    public String getUserSummary() { return userSummary; }
    public void setUserSummary(String userSummary) { this.userSummary = userSummary; }
}
