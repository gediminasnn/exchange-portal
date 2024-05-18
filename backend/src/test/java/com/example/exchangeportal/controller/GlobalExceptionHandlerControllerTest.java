package com.example.exchangeportal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerControllerTest {

    private final GlobalExceptionHandlerController handler = new GlobalExceptionHandlerController();

    @Test
    public void testhandleRuntimeException() {
        RuntimeException e = new RuntimeException("Test runtime exception");
        ResponseEntity<String> result = handler.handleRuntimeException(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Internal server error", result.getBody());
    }

     @Test
    public void testhandleApiException() {
        ApiException e = new ApiException("Test API exception");
        ResponseEntity<String> result = handler.handleApiException(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Internal server error", result.getBody());
    }

    @Test
    public void testhandleParsingException() {
        ParsingException e = new ParsingException("Test parsing exception");
        ResponseEntity<String> result = handler.handleParsingException(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Internal server error", result.getBody());
    }
}
