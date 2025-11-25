package com.payflow.wallet.dto.auth;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String username,
        String role,
        String expiresAt
) {}
