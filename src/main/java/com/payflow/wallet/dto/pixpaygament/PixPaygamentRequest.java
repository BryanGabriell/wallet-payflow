package com.payflow.wallet.dto.pixpaygament;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PixPaygamentRequest(
       @NotNull(message = "O valor e obrigatorio")
       @Positive(message = "O valor deve ser maior que zero")
       BigDecimal amount,
       @NotBlank(message = "A chave pix e obrigatoria")
       String pixKey,
       @NotNull(message = "O id nao pode ser nulo")
       @Positive(message = "O id deve ser positivo")
       Long transactionId
) {
}
