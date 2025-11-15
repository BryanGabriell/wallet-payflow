package com.payflow.wallet.enums.userenums;

public enum Role {
    ADMIN("Administrador"),
    USER("Usuário");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
