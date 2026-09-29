package com.safedriving.service;

import com.safedriving.dto.request.LoginRequest;
import com.safedriving.dto.request.RegisterRequest;
import com.safedriving.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
