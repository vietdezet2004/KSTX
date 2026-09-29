package com.safedriving.service.impl;

import com.safedriving.dto.request.LoginRequest;
import com.safedriving.dto.request.RegisterRequest;
import com.safedriving.dto.response.AuthResponse;
import com.safedriving.entity.Account;
import com.safedriving.entity.enums.AccountRole;
import com.safedriving.entity.enums.AccountStatus;
import com.safedriving.exception.BadRequestException;
import com.safedriving.repository.AccountRepository;
import com.safedriving.security.JwtTokenProvider;
import com.safedriving.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Xử lý đăng nhập cho người dùng: {}", request.getUsername());

        Account account = accountRepository.findByUsernameAndIsDeletedFalse(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Tên đăng nhập hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new BadRequestException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        if (account.getStatus() == AccountStatus.LOCKED) {
            throw new BadRequestException("Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên.");
        }

        if (account.getStatus() == AccountStatus.DISABLED) {
            throw new BadRequestException("Tài khoản đã bị vô hiệu hóa.");
        }

        LocalDateTime now = LocalDateTime.now();
        account.setLastLoginAt(now);
        accountRepository.save(account);

        String token = jwtTokenProvider.generateToken(
                account.getUsername(),
                account.getRole().name(),
                account.getId()
        );

        log.info("Người dùng {} đăng nhập thành công với vai trò {}", account.getUsername(), account.getRole());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationMs())
                .accountId(account.getId())
                .username(account.getUsername())
                .role(account.getRole())
                .status(account.getStatus())
                .lastLoginAt(now)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Xử lý đăng ký tài khoản mới: {}", request.getUsername());

        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Tên đăng nhập đã tồn tại trong hệ thống");
        }

        AccountRole role = AccountRole.DRIVER;

        Account account = Account.builder()
                .username(request.getUsername().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(AccountStatus.ACTIVE)
                .isDeleted(false)
                .build();

        Account savedAccount = accountRepository.save(account);

        String token = jwtTokenProvider.generateToken(
                savedAccount.getUsername(),
                savedAccount.getRole().name(),
                savedAccount.getId()
        );

        log.info("Đăng ký thành công tài khoản mới: {} (ID: {}, Role: {})",
                savedAccount.getUsername(), savedAccount.getId(), savedAccount.getRole());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationMs())
                .accountId(savedAccount.getId())
                .username(savedAccount.getUsername())
                .role(savedAccount.getRole())
                .status(savedAccount.getStatus())
                .lastLoginAt(savedAccount.getLastLoginAt())
                .build();
    }
}
