package com.safedriving.entity;

import com.safedriving.entity.enums.VehicleStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Vehicle extends BaseUuidEntity {

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    private String vin;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @lombok.Builder.Default
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    private Double odometerKm;

    @lombok.Builder.Default
    private Boolean isDeleted = false;
}
