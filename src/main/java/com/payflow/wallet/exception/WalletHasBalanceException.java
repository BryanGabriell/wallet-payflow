package com.payflow.wallet.exception;



public class WalletHasBalanceException extends RuntimeException {
    public WalletHasBalanceException(String message) {
        super(message);
    }
}
