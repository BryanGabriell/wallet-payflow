package com.payflow.wallet.controller;

import com.payflow.wallet.dto.passwordresettoken.ForgotPasswordRequest;
import com.payflow.wallet.dto.passwordresettoken.GenericMessageResponse;
import com.payflow.wallet.dto.passwordresettoken.ResetPasswordRequest;
import com.payflow.wallet.service.PasswordResetTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Recuperação de Senha",
        description = "Endpoints responsáveis pelo processo de recuperação e redefinição de senha dos usuários."
)
public class PasswordRecoveryController {

    private final PasswordResetTokenService tokenService;

    public PasswordRecoveryController(PasswordResetTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Solicitar recuperação de senha",
            description = "Inicia o processo de recuperação de senha. "
                    + "Caso o e-mail informado exista no sistema, "
                    + "um link de redefinição será enviado para o usuário."
    )
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


    @Operation(
            summary = "Redefinir senha",
            description = "Finaliza o processo de recuperação de senha utilizando um token válido. "
                    + "Após a validação do token, a senha do usuário é atualizada."
    )
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
