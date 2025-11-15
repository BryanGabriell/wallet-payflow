package com.payflow.wallet.enums.transactionsenums;


public enum TransactionType {
    PAGAMENTO("Pagamento"),
    DEPOSITO("Deposito"),
    SAQUE("Saque");

    private final String descricao;

    TransactionType(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
