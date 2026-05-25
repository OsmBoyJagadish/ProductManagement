package com.round1.services;

import com.round1.dto.request.LoginRequest;
import com.round1.dto.request.RegisterRequest;
import com.round1.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse registerAdmin(RegisterRequest request);
}
