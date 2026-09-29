package com.safedriving.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Lớp cha dùng chung cho các entity có khóa chính dạng UUID (CHAR(36)),
 * tương ứng với các bảng dùng CHAR(36) PRIMARY KEY trong schema gốc.
 * UUID được tự sinh khi persist nếu chưa có sẵn.
 *
 * Lưu ý: phải có @SuperBuilder ở đây thì các entity con dùng @SuperBuilder
 * mới biên dịch được (Lombok yêu cầu toàn bộ chuỗi kế thừa cùng dùng SuperBuilder).
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class BaseUuidEntity {

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
