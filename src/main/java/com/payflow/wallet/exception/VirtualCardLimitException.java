package com.payflow.wallet.exception;

public class VirtualCardLimitException extends RuntimeException {
    public VirtualCardLimitException(String message) {
        super(message);
    }
}
