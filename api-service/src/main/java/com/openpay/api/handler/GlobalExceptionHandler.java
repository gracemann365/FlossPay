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

/**
 * <h2>GlobalExceptionHandler</h2>
 * <p>
 * Centralized exception handling for all REST controllers in the API service.
 * Catches validation, business, and unexpected exceptions, logs them, and
 * formats
 * error responses for the client.
 * </p>
 *
 * <h3>Handled exception types:</h3>
 * <ul>
 * <li>Validation errors (e.g., bean validation, invalid fields)</li>
 * <li>Business rule errors (IllegalArgument, custom domain exceptions)</li>
 * <li>Uncaught/unexpected exceptions</li>
 * </ul>
 *
 * <h3>Usage:</h3>
 * <ul>
 * <li>Automatically applied to all controllers via
 * {@code @RestControllerAdvice}</li>
 * <li>Returns consistent error responses and logs internal errors for
 * debugging</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles bean validation errors (e.g., @Valid fails on DTOs).
     * Returns a map of field → error message with 400 Bad Request status.
     *
     * @param ex the thrown validation exception
     * @return response entity with error map and 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles IllegalArgumentException for business validation failures.
     * Returns error message as JSON with 400 Bad Request status.
     *
     * @param ex the thrown IllegalArgumentException
     * @return response entity with error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgs(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles any uncaught, generic exceptions.
     * Logs the error and returns a generic 500 Internal Server Error response.
     *
     * @param ex the thrown exception
     * @return response entity with generic error message and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }

    /**
     * Handles domain-specific UPI validation errors.
     *
     * @param ex the thrown InvalidUpiException
     * @return response entity with error message and 400 status
     */
    @ExceptionHandler(InvalidUpiException.class)
    public ResponseEntity<?> handleInvalidUpi(InvalidUpiException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles custom OpenPay business exceptions.
     *
     * @param ex the thrown OpenPayException
     * @return response entity with error message and 400 status
     */
    @ExceptionHandler(OpenPayException.class)
    public ResponseEntity<?> handleOpenPay(OpenPayException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
