package com.payflow.wallet.exception;


public class LimitTransactionException extends RuntimeException {
    public LimitTransactionException(String message) {
        super(message);
    }
}
