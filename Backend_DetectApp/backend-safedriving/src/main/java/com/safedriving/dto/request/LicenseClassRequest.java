package com.safedriving.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tạo mới hoặc cập nhật hạng giấy phép lái xe")
public class LicenseClassRequest {

    @NotNull(message = "ID của hạng bằng không được để trống")
    @Schema(description = "ID gán thủ công cho hạng bằng (VD: 1, 2, 3...)", example = "1")
    private Short id;

    @NotBlank(message = "Mã hạng bằng không được để trống")
    @Schema(description = "Mã hạng bằng lái xe (VD: B1, B2, C...)", example = "B2")
    private String code;

    @NotBlank(message = "Tên hạng bằng không được để trống")
    @Schema(description = "Tên đầy đủ của hạng bằng", example = "Hạng B2 - Xe ô tô chở người đến 9 chỗ")
    private String name;

    @NotNull(message = "Sức chứa tối đa không được để trống")
    @Schema(description = "Sức chứa tối đa của hạng bằng", example = "9")
    private Integer capacity;
}
