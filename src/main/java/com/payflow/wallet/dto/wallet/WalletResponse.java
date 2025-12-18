package com.payflow.wallet.dto.wallet;


import com.payflow.wallet.enums.walletenums.WalletStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponse(Long id,
                             BigDecimal balance,
                             WalletStatus status,
                             Long userId,
                             LocalDateTime createAt
) {
}
