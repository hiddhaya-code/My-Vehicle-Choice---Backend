package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.SavedComparison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedComparisonRepository extends JpaRepository<SavedComparison, Long> {
    List<SavedComparison> findByUserIdOrderByCreatedAtDesc(Long userId);
}
