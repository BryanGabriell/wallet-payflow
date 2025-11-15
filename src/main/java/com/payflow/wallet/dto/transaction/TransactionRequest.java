package com.payflow.wallet.dto.transaction;


import com.payflow.wallet.enums.transactionsenums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequest(@NotNull(message = "O ID da carteira remetente é obrigatório.")
                                  Long senderWalletId,

                                 @NotNull(message = "O ID da carteira destinatária é obrigatório.")
                                  Long receiverWalletId,

                                 @NotNull(message = "O valor da transação é obrigatório.")
                                  @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero.")
                                  @Digits(integer = 17, fraction = 2, message = "Valor inválido. Máximo de 17 dígitos e 2 casas decimais.")
                                 BigDecimal amount,

                                 @NotNull(message = "O tipo de transação é obrigatório.")
                                 TransactionType type) {
}
