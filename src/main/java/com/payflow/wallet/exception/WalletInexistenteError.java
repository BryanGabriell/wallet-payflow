package com.payflow.wallet.exception;

public class WalletInexistenteError extends RuntimeException {
    public WalletInexistenteError(String message) {
        super(message);
    }
}
