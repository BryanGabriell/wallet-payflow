package com.payflow.wallet.enums.pixpaygamentenum;



public enum PixPaygamentStatus {

    PENDENTE("Pendente"),
    PAGO("Pago"),
    CANCELADO("Cancelado"),
    EXPIRADO("Expirado");

    private final String descricao;

    PixPaygamentStatus(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
