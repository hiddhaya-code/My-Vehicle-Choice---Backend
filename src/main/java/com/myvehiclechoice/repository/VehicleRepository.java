package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.Vehicle;
import com.myvehiclechoice.model.enums.VehicleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    List<Vehicle> findByIsTrendingTrue();

    List<Vehicle> findByIsFeaturedTrue();

    List<Vehicle> findByCategory(VehicleCategory category);

    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.modelName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(v.brand.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Vehicle> searchByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT v FROM Vehicle v JOIN v.variants var WHERE " +
           "(:category IS NULL OR v.category = :category) AND " +
           "(:brandId IS NULL OR v.brand.id = :brandId) AND " +
           "(:bodyStyle IS NULL OR LOWER(v.bodyStyle) = LOWER(:bodyStyle)) AND " +
           "(:minPrice IS NULL OR var.estimatedOnRoadPrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR var.estimatedOnRoadPrice <= :maxPrice) GROUP BY v")
    Page<Vehicle> filterVehicles(
            @Param("category") VehicleCategory category,
            @Param("brandId") Long brandId,
            @Param("bodyStyle") String bodyStyle,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            Pageable pageable);
}
