package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.Dealer;
import com.myvehiclechoice.model.enums.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {

    List<Dealer> findByIsAuthorizedTrue();

    @Query("SELECT DISTINCT d FROM Dealer d " +
           "JOIN d.dealerBrands db " +
           "WHERE db.brand.id = :brandId " +
           "AND (:category IS NULL OR db.vehicleCategory = :category) " +
           "AND db.isAuthorized = true " +
           "AND (:dealerType IS NULL OR d.dealerType = :dealerType OR d.dealerType = 'SALES_AND_SERVICE') " +
           "AND (:city IS NULL OR LOWER(d.city) = LOWER(:city)) " +
           "AND (:state IS NULL OR LOWER(d.state) = LOWER(:state)) " +
           "AND (:pincode IS NULL OR d.pincode = :pincode)")
    List<Dealer> findAuthorizedDealers(
            @Param("brandId") Long brandId,
            @Param("category") VehicleCategory category,
            @Param("dealerType") String dealerType,
            @Param("city") String city,
            @Param("state") String state,
            @Param("pincode") String pincode
    );

    @Query("SELECT DISTINCT d FROM Dealer d " +
           "JOIN d.dealerBrands db " +
           "WHERE db.brand.id = :brandId " +
           "AND (:category IS NULL OR db.vehicleCategory = :category) " +
           "AND db.isAuthorized = true " +
           "AND (:dealerType IS NULL OR d.dealerType = :dealerType OR d.dealerType = 'SALES_AND_SERVICE')")
    List<Dealer> findAuthorizedDealersNoLocation(
            @Param("brandId") Long brandId,
            @Param("category") VehicleCategory category,
            @Param("dealerType") String dealerType
    );

    @Query("SELECT DISTINCT d FROM Dealer d " +
           "WHERE (:city IS NULL OR LOWER(d.city) = LOWER(:city)) " +
           "AND (:state IS NULL OR LOWER(d.state) = LOWER(:state)) " +
           "AND (:pincode IS NULL OR d.pincode = :pincode) " +
           "AND (:dealerType IS NULL OR d.dealerType = :dealerType OR d.dealerType = 'SALES_AND_SERVICE')")
    List<Dealer> findDealersByLocation(
            @Param("city") String city,
            @Param("state") String state,
            @Param("pincode") String pincode,
            @Param("dealerType") String dealerType
    );

    // Backward compatibility for existing simple lookup
    @Query("SELECT d FROM Dealer d WHERE " +
           "(:city IS NULL OR LOWER(d.city) = LOWER(:city)) AND " +
           "(:brandName IS NULL OR LOWER(d.brandName) LIKE LOWER(CONCAT('%', :brandName, '%')))")
    List<Dealer> findDealers(@Param("city") String city, @Param("brandName") String brandName);
}
