package com.payflow.wallet.dto.wallet;


import java.math.BigDecimal;

public record BalanceResponse(
        BigDecimal balance
) {
}
