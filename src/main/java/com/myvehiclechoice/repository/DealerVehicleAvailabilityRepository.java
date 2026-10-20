package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.DealerVehicleAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerVehicleAvailabilityRepository extends JpaRepository<DealerVehicleAvailability, Long> {

    List<DealerVehicleAvailability> findByDealerId(Long dealerId);

    List<DealerVehicleAvailability> findByVehicleId(Long vehicleId);

    Optional<DealerVehicleAvailability> findByDealerIdAndVehicleId(Long dealerId, Long vehicleId);
}
