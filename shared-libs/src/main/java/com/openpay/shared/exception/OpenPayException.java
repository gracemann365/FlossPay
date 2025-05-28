package com.openpay.shared.exception;

public class OpenPayException extends RuntimeException {
    public OpenPayException() {
        super();
    }

    public OpenPayException(String message) {
        super(message);
    }

    public OpenPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenPayException(Throwable cause) {
        super(cause);
    }
}
