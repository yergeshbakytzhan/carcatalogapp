package com.catalog.cars.service;

import com.catalog.cars.exception.RefreshTokenException;
import com.catalog.cars.model.auth.RefreshToken;
import com.catalog.cars.model.user.User;
import com.catalog.cars.properties.JwtProperties;
import com.catalog.cars.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtProperties jwtProperties = new JwtProperties();

    private final RefreshTokenRepository refreshTokenRepository;

    private final SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes(StandardCharsets.UTF_8));

    public RefreshToken generateAndSaveRefreshToken(User user){
        revokeOldRefreshTokens(user);
        var refreshTokenString = generateRefreshToken(user);
        return saveRefreshToken(refreshTokenString, user);
    }

    private String generateRefreshToken(User user){
        var uniqueTokenId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
                .claim("uniqueTokenId", uniqueTokenId)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private RefreshToken saveRefreshToken(String token, User user){
        var refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    private void revokeTokens(List<RefreshToken> tokens){
        tokens.forEach(token->{
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    public void revokeRefreshToken(RefreshToken refreshToken){
        var refreshTokenList = new ArrayList<RefreshToken>();
        refreshTokenList.add(refreshToken);
        revokeTokens(refreshTokenList);
    }

    private void revokeOldRefreshTokens(User user){
        var oldTokens = refreshTokenRepository.findByUserAndRevokedFalse(user);
        revokeTokens(oldTokens);
    }

    public void validateRefreshToken(RefreshToken refreshToken){
        if(refreshToken.getRevoked()){
            throw new RefreshTokenException("Refresh token has been revoked");
        }

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RefreshTokenException("Refresh token expired");
        }
    }

}
