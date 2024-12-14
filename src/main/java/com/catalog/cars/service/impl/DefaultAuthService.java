package com.catalog.cars.service.impl;

import com.catalog.cars.dto.AuthRequest;
import com.catalog.cars.dto.AuthResponse;
import com.catalog.cars.dto.RegisterRequest;
import com.catalog.cars.exception.NotFoundException;
import com.catalog.cars.exception.RefreshTokenException;
import com.catalog.cars.exception.UserAlreadyExistsException;
import com.catalog.cars.model.user.User;
import com.catalog.cars.repository.RefreshTokenRepository;
import com.catalog.cars.repository.UserRepository;
import com.catalog.cars.security.JWTUtil;
import com.catalog.cars.service.AuthService;
import com.catalog.cars.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthResponse authenticate(AuthRequest authRequest){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userDetails = (UserDetails)authentication.getPrincipal();
        var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()->{
            log.error("User not found by username: " + userDetails.getUsername());
            return new NotFoundException("User not found by username: " + userDetails.getUsername());
        });

        var accessToken = jwtUtil.generateToken(authRequest.getEmail(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        var refreshTokenEntity = refreshTokenService.generateAndSaveRefreshToken(user);

        return new AuthResponse(accessToken, refreshTokenEntity.getToken());
    }

    public AuthResponse register(RegisterRequest registerRequest){
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            log.error("User already exists");
            throw new UserAlreadyExistsException("User already exists");
        }

        var newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(registerRequest.getPassword());
        var roles = new ArrayList<String>();
        roles.add(newUser.getRole().name());

        userRepository.save(newUser);

        var accessToken = jwtUtil.generateToken(registerRequest.getEmail(), roles);
        var refreshToken = refreshTokenService.generateAndSaveRefreshToken(newUser);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refreshAccessToken(String refreshToken){
        var storedRefreshToken = refreshTokenRepository.findByToken(refreshToken).orElseThrow(()-> {
            log.error("Invalid refresh token");
            return new RefreshTokenException("Invalid refresh token");
        });

        refreshTokenService.validateRefreshToken(storedRefreshToken);
        refreshTokenService.revokeRefreshToken(storedRefreshToken);

        var user = storedRefreshToken.getUser();
        var roles = new ArrayList<String>();
        roles.add(user.getRole().name());

        var accessToken = jwtUtil.generateToken(user.getEmail(), roles);

        var newRefreshToken = refreshTokenService.generateAndSaveRefreshToken(user);

        return new AuthResponse(accessToken, newRefreshToken.getToken());
    }
}
