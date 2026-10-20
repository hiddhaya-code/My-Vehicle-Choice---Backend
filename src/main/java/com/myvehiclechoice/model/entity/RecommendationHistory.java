package com.myvehiclechoice.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_history")
public class RecommendationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "budget_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal budgetAmount;

    @Column(name = "budget_type", nullable = false, length = 50)
    private String budgetType;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "top_vehicle_ids", nullable = false)
    private String topVehicleIds;

    @Column(name = "explanation_summary", columnDefinition = "TEXT")
    private String explanationSummary;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public RecommendationHistory() {}

    public RecommendationHistory(User user, String sessionId, BigDecimal budgetAmount, String budgetType, 
                                 String category, String topVehicleIds, String explanationSummary) {
        this.user = user;
        this.sessionId = sessionId;
        this.budgetAmount = budgetAmount;
        this.budgetType = budgetType;
        this.category = category;
        this.topVehicleIds = topVehicleIds;
        this.explanationSummary = explanationSummary;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public String getBudgetType() { return budgetType; }
    public void setBudgetType(String budgetType) { this.budgetType = budgetType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTopVehicleIds() { return topVehicleIds; }
    public void setTopVehicleIds(String topVehicleIds) { this.topVehicleIds = topVehicleIds; }

    public String getExplanationSummary() { return explanationSummary; }
    public void setExplanationSummary(String explanationSummary) { this.explanationSummary = explanationSummary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
