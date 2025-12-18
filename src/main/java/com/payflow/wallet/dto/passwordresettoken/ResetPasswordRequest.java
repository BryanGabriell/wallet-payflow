package com.payflow.wallet.dto.passwordresettoken;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank
        String token,

        @NotBlank
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String newPassword
) {}
