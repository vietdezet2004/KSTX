package com.safedriving.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bảng danh mục (lookup table) - id gán thủ công (seed data), không auto-increment.
 * VD: B1, B2, C, D, E...
 */
@Entity
@Table(name = "license_class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseClass {

    @Id
    private Short id;

    @NotBlank(message = "Code is required")
    @Column(unique = true)
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    private Integer capacity;
}
