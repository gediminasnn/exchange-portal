package com.example.exchangeportal.exception;

public class BadApiResponseException extends ApiException {
    public BadApiResponseException() {
    }

    public BadApiResponseException(String message) {
        super(message);
    }

    public BadApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadApiResponseException(Throwable cause) {
        super(cause);
    }

    protected BadApiResponseException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
