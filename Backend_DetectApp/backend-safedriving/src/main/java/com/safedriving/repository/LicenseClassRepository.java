package com.safedriving.repository;

import com.safedriving.entity.LicenseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseClassRepository extends JpaRepository<LicenseClass, Short> {

    Optional<LicenseClass> findByCode(String code);

    boolean existsByCode(String code);
}
