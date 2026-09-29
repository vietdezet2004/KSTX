package com.safedriving.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Log vị trí xe - PK dạng BIGINT AUTO_INCREMENT (giống schema gốc), vì đây là bảng ghi log
 * tần suất cao, dùng số nguyên tăng dần tối ưu index hơn UUID.
 */
@Entity
@Table(name = "vehicle_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vehicle is required")
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Builder.Default
    private LocalDateTime timeVehicleLog = LocalDateTime.now();

    private BigDecimal lat;

    private BigDecimal lng;
}
