package com.safedriving.repository;

import com.safedriving.entity.VehicleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long> {

    List<VehicleLog> findByVehicleIdOrderByTimeVehicleLogDesc(String vehicleId);

    List<VehicleLog> findAllByOrderByTimeVehicleLogDesc();
}
