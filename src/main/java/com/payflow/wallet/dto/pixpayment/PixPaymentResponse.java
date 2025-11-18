package com.payflow.wallet.dto.pixpayment;


import com.payflow.wallet.enums.pixpaygamentenum.PixPaygamentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PixPaymentResponse(
        Long id,
        BigDecimal amount,
        String pixKey,
        String qrCodePayload,
        PixPaygamentStatus status,
        Long transactionId,
        LocalDateTime createdAt,
        LocalDateTime expirationTime,
        LocalDateTime paidAt,
        LocalDateTime cancelledAt,
        String cancellationReason
) {
}
