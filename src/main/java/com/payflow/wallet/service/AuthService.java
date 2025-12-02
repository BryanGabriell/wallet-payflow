package com.payflow.wallet.service;

import com.payflow.wallet.dto.auth.LoginRequest;
import com.payflow.wallet.dto.auth.LoginResponse;
import com.payflow.wallet.entity.RefreshToken;
import com.payflow.wallet.entity.User;
import com.payflow.wallet.enums.userenums.Role;
import com.payflow.wallet.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(3600);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("payflow-api")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("scope", "ROLE_" + user.getRole().name())
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                user.getEmail(),
                user.getRole().name(),
                formatter.format(expiresAt)
        );
    }

    public Long getLoggedUserId() {
        String subject = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return Long.parseLong(subject);
    }

    public Role getLoggedUserRoleEnum() {
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();  // "ROLE_USER"

        return Role.valueOf(role.replace("ROLE_", ""));
    }

    }



