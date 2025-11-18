package com.payflow.wallet.dto.pixpayment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PixPaymentRequest(
       @NotNull(message = "O valor e obrigatorio")
       @Positive(message = "O valor deve ser maior que zero")
       BigDecimal amount,
       @NotBlank(message = "A chave pix e obrigatoria")
       String pixKey
) {
}
