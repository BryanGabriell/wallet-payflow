package com.payflow.wallet.exception;



public class UserAlreadyHasWalletException extends RuntimeException {
    public UserAlreadyHasWalletException(String message) {
        super(message);
    }
}
