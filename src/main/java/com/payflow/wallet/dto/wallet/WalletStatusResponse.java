package com.payflow.wallet.dto.wallet;


import com.payflow.wallet.enums.walletenums.WalletStatus;

public record WalletStatusResponse(
        WalletStatus walletStatus
) {
}
