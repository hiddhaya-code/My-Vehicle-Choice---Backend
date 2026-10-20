package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
}
