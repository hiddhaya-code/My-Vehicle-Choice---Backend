package com.myvehiclechoice.controller;

import com.myvehiclechoice.dto.ApiResponse;
import com.myvehiclechoice.dto.recommendation.QuestionnaireAnswersDto;
import com.myvehiclechoice.dto.recommendation.RecommendationResponseDto;
import com.myvehiclechoice.model.entity.RecommendationHistory;
import com.myvehiclechoice.repository.RecommendationHistoryRepository;
import com.myvehiclechoice.repository.UserRepository;
import com.myvehiclechoice.service.RecommendationEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Recommendation Engine", description = "Deterministic, explainable recommendation scoring API")
public class RecommendationController {

    private final RecommendationEngineService recommendationService;
    private final RecommendationHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public RecommendationController(RecommendationEngineService recommendationService,
                                    RecommendationHistoryRepository historyRepository,
                                    UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/recommendations/evaluate")
    @Operation(summary = "Calculate ranked vehicle recommendations based on budget and priorities")
    public ResponseEntity<ApiResponse<RecommendationResponseDto>> evaluate(@RequestBody QuestionnaireAnswersDto answers,
                                                                             @AuthenticationPrincipal UserDetails userDetails) {
        RecommendationResponseDto response = recommendationService.evaluateRecommendations(answers);

        // Optionally persist history if user is signed in
        if (userDetails != null) {
            recommendationService.saveRecommendationHistory(userDetails.getUsername(), answers, response);
        }

        return ResponseEntity.ok(ApiResponse.ok("Recommendations calculated successfully", response));
    }

    @GetMapping("/users/me/recommendations")
    @Operation(summary = "Get user recommendation search history")
    public ResponseEntity<ApiResponse<List<RecommendationHistory>>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        var user = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
        }
        List<RecommendationHistory> list = historyRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        return ResponseEntity.ok(ApiResponse.ok("Recommendation history retrieved", list));
    }
}
