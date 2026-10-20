package com.myvehiclechoice.model.entity;

import com.myvehiclechoice.model.enums.VehicleCategory;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles", uniqueConstraints = {
    @UniqueConstraint(name = "uk_brand_model", columnNames = {"brand_id", "model_name"})
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(name = "model_name", nullable = false, length = 150)
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private VehicleCategory category;

    @Column(name = "body_style", length = 50)
    private String bodyStyle;

    @Column(length = 255)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "hero_image_url", length = 500)
    private String heroImageUrl;

    @Column(name = "is_trending")
    private Boolean isTrending = false;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehicleVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehicleImage> images = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Vehicle() {}

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

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public VehicleCategory getCategory() { return category; }
    public void setCategory(VehicleCategory category) { this.category = category; }

    public String getBodyStyle() { return bodyStyle; }
    public void setBodyStyle(String bodyStyle) { this.bodyStyle = bodyStyle; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHeroImageUrl() { return heroImageUrl; }
    public void setHeroImageUrl(String heroImageUrl) { this.heroImageUrl = heroImageUrl; }

    public Boolean getIsTrending() { return isTrending; }
    public void setIsTrending(Boolean isTrending) { this.isTrending = isTrending; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public List<VehicleVariant> getVariants() { return variants; }
    public void setVariants(List<VehicleVariant> variants) { this.variants = variants; }

    public List<VehicleImage> getImages() { return images; }
    public void setImages(List<VehicleImage> images) { this.images = images; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
