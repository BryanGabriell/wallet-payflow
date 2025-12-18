package com.payflow.wallet.controller;

import com.payflow.wallet.dto.passwordresettoken.ForgotPasswordRequest;
import com.payflow.wallet.dto.passwordresettoken.GenericMessageResponse;
import com.payflow.wallet.dto.passwordresettoken.ResetPasswordRequest;
import com.payflow.wallet.service.PasswordResetTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class PasswordRecoveryController {

    private final PasswordResetTokenService tokenService;

    public PasswordRecoveryController(PasswordResetTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<GenericMessageResponse> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request) {

        tokenService.generateAndSendToken(request.email());

        return ResponseEntity.ok(
                new GenericMessageResponse(
                        "Se o email existir, enviaremos as instruções"
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GenericMessageResponse> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request) {

        tokenService.resetPassword(
                request.token(),
                request.newPassword()
        );

        return ResponseEntity.ok(
                new GenericMessageResponse("Senha atualizada com sucesso")
        );
    }
}
