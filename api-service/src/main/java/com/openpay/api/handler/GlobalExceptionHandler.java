package com.openpay.api.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.openpay.shared.exception.InvalidUpiException;
import com.openpay.shared.exception.OpenPayException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger for printing error stack traces to your console/logs
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles bean validation errors (e.g., @Valid fails on your DTO)
     * Returns a map of field -> error message, with 400 status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles common business validation errors (e.g., IllegalArgumentException in
     * your service)
     * Returns error message as JSON with 400 status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgs(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles all other uncaught exceptions.
     * LOGS the error with stack trace for debugging,
     * and returns a generic error message to the client with 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Internal server error", ex); // <-- This will log the full stack trace
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }

    // Custom Exception Handlers for invalid upi and openpay

    @ExceptionHandler(InvalidUpiException.class)
    public ResponseEntity<?> handleInvalidUpi(InvalidUpiException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(OpenPayException.class)
    public ResponseEntity<?> handleOpenPay(OpenPayException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

}
