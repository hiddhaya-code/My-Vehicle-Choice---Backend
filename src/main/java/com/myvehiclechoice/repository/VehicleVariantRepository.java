package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.VehicleVariant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleVariantRepository extends JpaRepository<VehicleVariant, Long> {

    List<VehicleVariant> findByVehicleIdOrderByEstimatedOnRoadPriceAsc(Long vehicleId);

    @Query("SELECT var FROM VehicleVariant var " +
           "JOIN var.vehicle v " +
           "JOIN v.brand b " +
           "WHERE LOWER(v.modelName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "   OR LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "   OR LOWER(var.variantName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY var.estimatedOnRoadPrice ASC")
    List<VehicleVariant> searchVariantsForCompare(@Param("query") String query, Pageable pageable);
}
