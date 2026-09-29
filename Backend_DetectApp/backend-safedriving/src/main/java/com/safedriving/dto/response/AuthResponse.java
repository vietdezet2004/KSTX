package com.safedriving.dto.response;

import com.safedriving.entity.enums.AccountRole;
import com.safedriving.entity.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin phản hồi sau khi xác thực thành công")
public class AuthResponse {

    @Schema(description = "JWT Access Token")
    private String accessToken;

    @Builder.Default
    @Schema(description = "Loại Token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Thời gian hết hạn của token (tính bằng milliseconds)", example = "3600000")
    private Long expiresIn;

    @Schema(description = "ID của tài khoản")
    private String accountId;

    @Schema(description = "Tên đăng nhập")
    private String username;

    @Schema(description = "Vai trò người dùng")
    private AccountRole role;

    @Schema(description = "Trạng thái tài khoản")
    private AccountStatus status;

    @Schema(description = "Thời gian đăng nhập gần nhất")
    private LocalDateTime lastLoginAt;
}
