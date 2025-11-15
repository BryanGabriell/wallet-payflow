package com.payflow.wallet.enums.transactionsenums;



public enum TransactionStatus {
    PENDENTE("Pendente"),
    CONCLUIDO("Concluido"),
    FALHADO("Falhado");

    private final String descricao;

    TransactionStatus(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
