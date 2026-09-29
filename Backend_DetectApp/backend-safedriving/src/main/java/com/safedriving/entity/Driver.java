package com.safedriving.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Driver extends BaseUuidEntity {

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @Column(columnDefinition = "TEXT")
    private String urlImage;

    @lombok.Builder.Default
    private Boolean isActive = true;

    @NotNull(message = "Staff info is required")
    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @lombok.Builder.Default
    private Boolean isDeleted = false;
}
