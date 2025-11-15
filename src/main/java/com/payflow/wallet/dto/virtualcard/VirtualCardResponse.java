package com.payflow.wallet.dto.virtualcard;


import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VirtualCardResponse(
        Long id,
        String maskedCardNumber,
        BigDecimal cardLimit,
        VirtualCardStatus status,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updateAt

) {
}
