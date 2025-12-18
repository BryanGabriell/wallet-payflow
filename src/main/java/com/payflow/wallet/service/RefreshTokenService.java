package com.payflow.wallet.service;

import com.payflow.wallet.dto.refreshtoken.RefreshRequest;
import com.payflow.wallet.dto.refreshtoken.RefreshResponse;
import com.payflow.wallet.entity.RefreshToken;
import com.payflow.wallet.entity.User;
import com.payflow.wallet.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtEncoder jwtEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtEncoder = jwtEncoder;
    }

    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.findByUser(user)
                .ifPresent(old -> refreshTokenRepository.delete(old));

        LocalDateTime now = LocalDateTime.now();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(now)
                .expiresAt(now.plus(Duration.ofMillis(refreshTokenExpiration)))
                .revoked(false)
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado.");
        }
        return token;
    }

    public RefreshResponse refresh(RefreshRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        verifyExpiration(refreshToken);

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(3600); // 1 hora

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("payflow-api")
                .subject(refreshToken.getUser().getId().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("scope", "ROLE_" + refreshToken.getUser().getRole().name())
                .build();

        String newAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new RefreshResponse(
                newAccessToken,
                refreshToken.getToken(),
                "Bearer",
                expiresAt.toString()
        );
    }
}
