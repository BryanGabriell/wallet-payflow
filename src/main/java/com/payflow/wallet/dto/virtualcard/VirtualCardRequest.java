package com.payflow.wallet.dto.virtualcard;


import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record VirtualCardRequest(
        @NotNull(message = "O valor e obrigatorio")
        @Positive(message = "O limite deve ser maior que zero")
        BigDecimal cardLimit,
        @NotNull(message = "O status deve ser obrigatorio")
        VirtualCardStatus cardStatus
) {
}
