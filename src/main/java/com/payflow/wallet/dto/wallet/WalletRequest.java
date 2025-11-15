package com.payflow.wallet.dto.wallet;


import com.payflow.wallet.enums.walletenums.WalletStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WalletRequest(
        @NotNull(message = "O saldo não pode ser nulo")
        @DecimalMin(value = "0.00", inclusive = true, message = "O saldo inicial deve ser zero ou positivo")
        BigDecimal balance,

        @NotNull(message = "O status da carteira é obrigatório")
        WalletStatus status,

        @NotNull(message = "O ID do usuário é obrigatório")
        Long userId) {
}
