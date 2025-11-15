package com.payflow.wallet.dto.wallet;


import com.payflow.wallet.enums.walletenums.WalletStatus;

import java.math.BigDecimal;

public record WalletResponse(Long id,
                             BigDecimal balance,
                             WalletStatus status,
                             Long userId,
                             String username,
                             String email) {
}
