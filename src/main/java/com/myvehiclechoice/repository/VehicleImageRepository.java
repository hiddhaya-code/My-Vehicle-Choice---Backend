package com.myvehiclechoice.repository;

import com.myvehiclechoice.model.entity.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleImageRepository extends JpaRepository<VehicleImage, Long> {
    List<VehicleImage> findByVehicleIdOrderByDisplayOrderAsc(Long vehicleId);
}
