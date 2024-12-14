package com.catalog.cars.service;

import com.catalog.cars.dto.AuthRequest;
import com.catalog.cars.dto.AuthResponse;
import com.catalog.cars.dto.RegisterRequest;

public interface AuthService {

    AuthResponse authenticate(AuthRequest authRequest);

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse refreshAccessToken(String refreshToken);
}
