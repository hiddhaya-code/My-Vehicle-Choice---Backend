package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.DealerEnquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerEnquiryRepository extends JpaRepository<DealerEnquiry, Long> {
    List<DealerEnquiry> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<DealerEnquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByStatus(String status);
}
