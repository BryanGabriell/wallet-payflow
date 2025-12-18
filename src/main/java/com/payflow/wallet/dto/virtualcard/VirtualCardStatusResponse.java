package com.payflow.wallet.dto.virtualcard;

import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;

public record VirtualCardStatusResponse(
        VirtualCardStatus virtualCardStatus
) {
}
