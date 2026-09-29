package com.safedriving.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.safedriving.entity.enums.AccountRole;
import com.safedriving.entity.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Account extends BaseUuidEntity {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // nhận password khi tạo/sửa, nhưng không bao giờ trả về trong response
    private String password;

    @Enumerated(EnumType.STRING)
    @lombok.Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @lombok.Builder.Default
    private Boolean isDeleted = false;

    private LocalDateTime lastLoginAt;
}
