package com.example.exchangeportal.exception;

public class BadHttpClientRequestException extends ApiException {
    public BadHttpClientRequestException() {
    }

    public BadHttpClientRequestException(String message) {
        super(message);
    }

    public BadHttpClientRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadHttpClientRequestException(Throwable cause) {
        super(cause);
    }

    protected BadHttpClientRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
