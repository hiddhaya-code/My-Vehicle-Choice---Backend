package com.myvehiclechoice.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_audit_log")
public class DataAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "field_name", nullable = false, length = 50)
    private String fieldName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    @Column(name = "audit_reason", nullable = false, length = 255)
    private String auditReason;

    @Column(name = "source_reference", nullable = false, length = 255)
    private String sourceReference;

    @Column(name = "audit_date", nullable = false)
    private LocalDate auditDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public DataAuditLog() {}

    public DataAuditLog(String tableName, Long recordId, String fieldName, String oldValue, String newValue,
                        String actionType, String auditReason, String sourceReference, LocalDate auditDate) {
        this.tableName = tableName;
        this.recordId = recordId;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.actionType = actionType;
        this.auditReason = auditReason;
        this.sourceReference = sourceReference;
        this.auditDate = auditDate;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getAuditReason() { return auditReason; }
    public void setAuditReason(String auditReason) { this.auditReason = auditReason; }

    public String getSourceReference() { return sourceReference; }
    public void setSourceReference(String sourceReference) { this.sourceReference = sourceReference; }

    public LocalDate getAuditDate() { return auditDate; }
    public void setAuditDate(LocalDate auditDate) { this.auditDate = auditDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
