package com.safedriving.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin hạng bằng lái xe")
public class LicenseClassResponse {

    @Schema(description = "ID hạng bằng")
    private Short id;

    @Schema(description = "Mã hạng bằng (B1, B2, C, D, E...)")
    private String code;

    @Schema(description = "Tên đầy đủ của hạng bằng lái")
    private String name;

    @Schema(description = "Dung tích/số chỗ quy định")
    private Integer capacity;
}
