package com.payflow.wallet.exception;


public class UsuarioInexistenteError extends RuntimeException {
    public UsuarioInexistenteError(String message) {
        super(message);
    }
}
