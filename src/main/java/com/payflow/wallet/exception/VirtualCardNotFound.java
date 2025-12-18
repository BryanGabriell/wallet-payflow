package com.payflow.wallet.exception;


public class VirtualCardNotFound extends RuntimeException {
    public VirtualCardNotFound(String message) {
        super(message);
    }
}
