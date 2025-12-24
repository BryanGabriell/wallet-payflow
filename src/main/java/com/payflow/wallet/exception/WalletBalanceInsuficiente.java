package com.payflow.wallet.exception;


public class WalletBalanceInsuficiente extends RuntimeException {
    public WalletBalanceInsuficiente(String message) {
        super(message);
    }
}
