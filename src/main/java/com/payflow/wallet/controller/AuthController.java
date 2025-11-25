package com.payflow.wallet.controller;

import com.payflow.wallet.dto.auth.LoginRequest;
import com.payflow.wallet.dto.auth.LoginResponse;
import com.payflow.wallet.dto.refreshtoken.RefreshRequest;
import com.payflow.wallet.dto.refreshtoken.RefreshResponse;
import com.payflow.wallet.service.AuthService;
import com.payflow.wallet.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        return ResponseEntity.ok(refreshTokenService.refresh(request));
    }
}
