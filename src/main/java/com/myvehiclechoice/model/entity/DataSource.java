package com.myvehiclechoice.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_sources")
public class DataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_name", nullable = false, unique = true, length = 150)
    private String sourceName;

    @Column(name = "source_type", nullable = false, length = 50)
    private String sourceType;

    @Column(name = "base_url", length = 500)
    private String baseUrl;

    @Column(name = "verification_date")
    private LocalDate verificationDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public DataSource() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public LocalDate getVerificationDate() { return verificationDate; }
    public void setVerificationDate(LocalDate verificationDate) { this.verificationDate = verificationDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
