package com.payflow.wallet.enums.walletenums;



public enum WalletStatus {
    ATIVA("Ativa")
    ,BLOQUEADA("Bloqueada");

    private final String descricao;

    WalletStatus(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
