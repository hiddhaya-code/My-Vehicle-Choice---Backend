package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.DataAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataAuditLogRepository extends JpaRepository<DataAuditLog, Long> {

    List<DataAuditLog> findByTableName(String tableName);

    List<DataAuditLog> findByTableNameAndRecordId(String tableName, Long recordId);

    List<DataAuditLog> findByActionType(String actionType);
}
