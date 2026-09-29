package com.safedriving.repository;

import com.safedriving.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    Optional<Vehicle> findByIdAndIsDeletedFalse(String id);

    boolean existsByIdAndIsDeletedFalse(String id);
}
