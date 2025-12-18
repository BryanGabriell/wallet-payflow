package com.payflow.wallet.dto.wallet;


import com.payflow.wallet.enums.walletenums.WalletStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WalletRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long userId
)
{}
