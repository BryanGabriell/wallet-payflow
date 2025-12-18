package com.payflow.wallet.exception;

public class EmailInexistenteError extends RuntimeException {
    public EmailInexistenteError(String message) {
        super(message);
    }
}
