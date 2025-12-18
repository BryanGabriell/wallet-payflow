package com.payflow.wallet.exception;

public class VirtualCardInactiveException extends RuntimeException {
    public VirtualCardInactiveException(String message) {
        super(message);
    }
}
