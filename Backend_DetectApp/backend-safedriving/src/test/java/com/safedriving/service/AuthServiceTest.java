package com.safedriving.service;

import com.safedriving.dto.request.LoginRequest;
import com.safedriving.dto.request.RegisterRequest;
import com.safedriving.dto.response.AuthResponse;
import com.safedriving.entity.Account;
import com.safedriving.entity.enums.AccountRole;
import com.safedriving.entity.enums.AccountStatus;
import com.safedriving.exception.BadRequestException;
import com.safedriving.repository.AccountRepository;
import com.safedriving.security.JwtTokenProvider;
import com.safedriving.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .id("account-uuid-123")
                .username("testuser")
                .password("encodedPassword")
                .role(AccountRole.ADMIN)
                .status(AccountStatus.ACTIVE)
                .isDeleted(false)
                .build();
    }

    @Test
    @DisplayName("Register - Thành công khi username chưa tồn tại")
    void register_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .password("plainPassword")
                .build();

        when(accountRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            acc.setId("new-uuid-456");
            return acc;
        });
        when(jwtTokenProvider.generateToken(anyString(), anyString(), anyString())).thenReturn("generated-jwt-token");
        when(jwtTokenProvider.getJwtExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("generated-jwt-token", response.getAccessToken());
        assertEquals("newuser", response.getUsername());
        assertEquals(AccountRole.DRIVER, response.getRole());
        verify(accountRepository).existsByUsername("newuser");
    }

    @Test
    @DisplayName("Register - Thất bại khi username đã tồn tại")
    void register_ThrowsBadRequestException_WhenUsernameExists() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .password("plainPassword")
                .build();

        when(accountRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("Login - Thành công với thông tin đúng")
    void login_Success() {
        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("plainPassword")
                .build();

        when(accountRepository.findByUsernameAndIsDeletedFalse("testuser")).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken("testuser", "ADMIN", "account-uuid-123")).thenReturn("jwt-token-123");
        when(jwtTokenProvider.getJwtExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.getAccessToken());
        assertEquals("testuser", response.getUsername());
        assertEquals(AccountRole.ADMIN, response.getRole());
        assertNotNull(testAccount.getLastLoginAt());
    }

    @Test
    @DisplayName("Login - Thất bại khi mật khẩu không khớp")
    void login_ThrowsBadRequestException_WhenPasswordWrong() {
        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("wrongPassword")
                .build();

        when(accountRepository.findByUsernameAndIsDeletedFalse("testuser")).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("Login - Thất bại khi tài khoản bị khóa")
    void login_ThrowsBadRequestException_WhenAccountLocked() {
        testAccount.setStatus(AccountStatus.LOCKED);
        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("plainPassword")
                .build();

        when(accountRepository.findByUsernameAndIsDeletedFalse("testuser")).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }
}
