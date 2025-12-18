package com.payflow.wallet.exception;


public class TransactionPendingException extends RuntimeException {
    public TransactionPendingException(String message) {
        super(message);
    }
}
