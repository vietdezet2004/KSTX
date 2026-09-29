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

import java.time.LocalDateTime;

@Entity
@Table(name = "somnolence_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SomnolenceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Driver is required")
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Builder.Default
    private LocalDateTime recordTime = LocalDateTime.now();

    private String notes;
}