package com.myvehiclechoice.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_comparisons")
public class SavedComparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "variant_ids", nullable = false)
    private String variantIds;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public SavedComparison() {}

    public SavedComparison(User user, String title, String variantIds) {
        this.user = user;
        this.title = title;
        this.variantIds = variantIds;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getVariantIds() { return variantIds; }
    public void setVariantIds(String variantIds) { this.variantIds = variantIds; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
