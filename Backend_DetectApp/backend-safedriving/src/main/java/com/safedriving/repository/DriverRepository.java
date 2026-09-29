package com.safedriving.repository;

import com.safedriving.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {

    Optional<Driver> findByIdAndIsDeletedFalse(String id);
}
