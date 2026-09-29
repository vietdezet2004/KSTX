package com.safedriving.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin yêu cầu đăng nhập")
public class LoginRequest {

    @NotBlank(message = "Username không được để trống")
    @Schema(description = "Tên đăng nhập", example = "admin")
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Schema(description = "Mật khẩu tài khoản", example = "password123")
    private String password;
}
