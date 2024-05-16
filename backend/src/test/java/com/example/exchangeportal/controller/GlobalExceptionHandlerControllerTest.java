package com.example.exchangeportal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerControllerTest {

    private final GlobalExceptionHandlerController handler = new GlobalExceptionHandlerController();

    @Test
    public void handleRuntimeException() {
        RuntimeException e = new RuntimeException("Test runtime exception");
        ResponseEntity<String> result = handler.handleRuntimeException(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Internal server error", result.getBody());
    }
}
