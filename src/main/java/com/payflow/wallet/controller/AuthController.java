package com.payflow.wallet.controller;

import com.payflow.wallet.dto.auth.LoginRequest;
import com.payflow.wallet.dto.auth.LoginResponse;
import com.payflow.wallet.dto.refreshtoken.RefreshRequest;
import com.payflow.wallet.dto.refreshtoken.RefreshResponse;
import com.payflow.wallet.service.AuthService;
import com.payflow.wallet.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints responsáveis pela autenticação de usuários e geração de tokens JWT.")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }
    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza a autenticação do usuário com e-mail e senha. "
                    + "Em caso de sucesso, retorna um token JWT e informações de autenticação."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(200).body(loginResponse);
    }

    @Operation(
            summary = "Renovar token de autenticação",
            description = "Gera um novo token de acesso a partir de um refresh token válido, "
                    + "permitindo que o usuário continue autenticado sem realizar novo login."
    )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        RefreshResponse refreshResponse = refreshTokenService.refresh(request);
        return ResponseEntity.status(200).body(refreshResponse);
    }
}
