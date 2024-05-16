package com.example.exchangeportal.exception;

public class FailedParsingException extends ParsingException {
    public FailedParsingException() {
    }

    public FailedParsingException(String message) {
        super(message);
    }

    public FailedParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedParsingException(Throwable cause) {
        super(cause);
    }

    protected FailedParsingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
