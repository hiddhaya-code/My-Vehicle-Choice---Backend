package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.DealerBrand;
import com.myvehiclechoice.model.enums.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerBrandRepository extends JpaRepository<DealerBrand, Long> {

    List<DealerBrand> findByDealerId(Long dealerId);

    List<DealerBrand> findByBrandId(Long brandId);

    List<DealerBrand> findByBrandIdAndVehicleCategory(Long brandId, VehicleCategory category);

    @Query("SELECT db FROM DealerBrand db WHERE db.brand.id = :brandId AND db.vehicleCategory = :category AND db.isAuthorized = true")
    List<DealerBrand> findAuthorizedByBrandAndCategory(@Param("brandId") Long brandId, @Param("category") VehicleCategory category);
}
