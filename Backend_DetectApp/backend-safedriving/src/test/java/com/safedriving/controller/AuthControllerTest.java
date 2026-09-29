package com.safedriving.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safedriving.dto.request.LoginRequest;
import com.safedriving.dto.request.RegisterRequest;
import com.safedriving.dto.response.AuthResponse;
import com.safedriving.entity.enums.AccountRole;
import com.safedriving.entity.enums.AccountStatus;
import com.safedriving.exception.BadRequestException;
import com.safedriving.security.JwtAuthenticationEntryPoint;
import com.safedriving.security.JwtAuthenticationFilter;
import com.safedriving.security.JwtTokenProvider;
import com.safedriving.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @DisplayName("POST /auth/register - Đăng ký tài khoản thành công")
    void register_Success() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("driver_test")
                .password("password123")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("mock-jwt-token")
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .accountId("mock-id")
                .username("driver_test")
                .role(AccountRole.DRIVER)
                .status(AccountStatus.ACTIVE)
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Đăng ký tài khoản thành công"))
                .andExpect(jsonPath("$.data.accessToken").value("mock-jwt-token"))
                .andExpect(jsonPath("$.data.username").value("driver_test"))
                .andExpect(jsonPath("$.data.role").value("DRIVER"));
    }

    @Test
    @DisplayName("POST /auth/register - Đăng ký thất bại khi username đã tồn tại")
    void register_UsernameExists() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("driver_test")
                .password("password123")
                .build();

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new BadRequestException("Tên đăng nhập đã tồn tại trong hệ thống"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Tên đăng nhập đã tồn tại trong hệ thống"));
    }

    @Test
    @DisplayName("POST /auth/login - Đăng nhập thành công")
    void login_Success() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("driver_test")
                .password("password123")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("mock-jwt-token")
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .accountId("mock-id")
                .username("driver_test")
                .role(AccountRole.DRIVER)
                .status(AccountStatus.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Đăng nhập thành công"))
                .andExpect(jsonPath("$.data.accessToken").value("mock-jwt-token"))
                .andExpect(jsonPath("$.data.username").value("driver_test"));
    }

    @Test
    @DisplayName("POST /auth/login - Đăng nhập thất bại khi sai mật khẩu")
    void login_InvalidCredentials() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("driver_test")
                .password("wrongpassword")
                .build();

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadRequestException("Tên đăng nhập hoặc mật khẩu không chính xác"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("GET /auth/health - Kiểm tra sức khỏe dịch vụ")
    void healthCheck_Success() throws Exception {
        mockMvc.perform(get("/auth/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("UP"));
    }
}
