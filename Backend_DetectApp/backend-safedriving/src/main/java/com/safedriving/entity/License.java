package com.safedriving.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "license")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class License extends BaseUuidEntity {

    @NotBlank(message = "License number is required")
    @Column(unique = true, nullable = false)
    private String licenseNo;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "License expiry date is required")
    private LocalDate licenseExpiry;

    @NotBlank(message = "Place of issue is required")
    private String placeOfIssue;

    @NotNull(message = "Driver is required")
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @NotNull(message = "License class is required")
    @ManyToOne
    @JoinColumn(name = "license_class_id")
    private LicenseClass licenseClass;

    @lombok.Builder.Default
    private Boolean isDeleted = false;
}
