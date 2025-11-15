package com.payflow.wallet.enums.cardvirtualenum;

public enum VirtualCardStatus {

    ATIVO("Ativo"),
    BLOQUEADO("Bloqueado");

    String descricao;

    VirtualCardStatus(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
