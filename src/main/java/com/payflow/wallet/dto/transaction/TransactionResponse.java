package com.payflow.wallet.dto.transaction;


import com.payflow.wallet.enums.transactionsenums.TransactionStatus;
import com.payflow.wallet.enums.transactionsenums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id,
                                  BigDecimal amount,
                                  TransactionType type,
                                  TransactionStatus status,
                                  Long senderWalletId,
                                  Long receiverWalletId,
                                  LocalDateTime createdAt) {
}
