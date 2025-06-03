package com.openpay.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpay.api.security.HmacAuthService;
import com.openpay.api.service.TransactionApiProducer;
import com.openpay.shared.dto.PaymentRequest;
import com.openpay.shared.dto.StatusResponse;

import jakarta.validation.Valid;

/**
 * ========================================================================
 * TransactionController: OpenPay Payment & Collect Endpoints (API-Hardening
 * Edition)
 * ------------------------------------------------------------------------
 * - Initiates new UPI payment requests (/pay).
 * - Initiates new UPI collect requests (/collect).
 * - Enforces API-level HMAC authentication for all money movement endpoints.
 * - Validates request input, idempotency, and authenticates using HMAC.
 * - Delegates business logic to TransactionApiProducer (service layer).
 * ========================================================================
 * <b>API Endpoints:</b>
 * POST /pay - Initiate a payment
 * POST /collect - Initiate a collect (pull) request
 * Headers: Idempotency-Key (required), X-HMAC (required)
 * Body: PaymentRequest
 * ========================================================================
 * <b>Security/Audit:</b>
 * - All requests must include valid HMAC, or will be rejected (401/403).
 * - All failures are reported with appropriate HTTP status for traceability.
 * ========================================================================
 * 
 * @author David Grace
 * @since 1.0
 */
@RestController
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionApiProducer transactionApiProducer;
    private final HmacAuthService hmacAuthService;
    private final ObjectMapper objectMapper;

    public TransactionController(TransactionApiProducer transactionApiProducer,
            HmacAuthService hmacAuthService,
            ObjectMapper objectMapper) {
        this.transactionApiProducer = transactionApiProducer;
        this.hmacAuthService = hmacAuthService;
        this.objectMapper = objectMapper;
    }

    /**
     * Handles payment initiation requests with HMAC authentication.
     */
    @PostMapping("/pay")
    public ResponseEntity<StatusResponse> initiatePayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader(value = "X-HMAC", required = false) String hmacHeader) {

        return handleHmacProtectedRequest(
                request,
                idempotencyKey,
                hmacHeader,
                (req, key) -> {
                    Long id = transactionApiProducer.createTransaction(req, key);
                    return new StatusResponse(id, "QUEUED", "Transaction queued");
                });
    }

    /**
     * Handles collect initiation requests with HMAC authentication.
     */
    @PostMapping("/collect")
    public ResponseEntity<StatusResponse> initiateCollect(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader(value = "X-HMAC", required = false) String hmacHeader) {

        return handleHmacProtectedRequest(
                request,
                idempotencyKey,
                hmacHeader,
                (req, key) -> {
                    Long id = transactionApiProducer.createCollectRequest(req, key);
                    return new StatusResponse(id, "REQUESTED", "Collect request queued");
                });
    }

    // =========================
    // INTERNAL HELPER (DRY)
    // =========================

    /**
     * Centralized HMAC/auth/error handling for DRY code across endpoints.
     */
    private ResponseEntity<StatusResponse> handleHmacProtectedRequest(
            PaymentRequest request,
            String idempotencyKey,
            String hmacHeader,
            BusinessLogic logic) {

        if (hmacHeader == null || hmacHeader.isBlank()) {
            log.warn("[SECURITY] Request missing HMAC header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(null, "ERROR", "Missing HMAC header"));
        }

        String message;
        try {
            message = objectMapper.writeValueAsString(request) + idempotencyKey;
        } catch (JsonProcessingException e) {
            log.error("[SECURITY] Failed to serialize PaymentRequest for HMAC validation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StatusResponse(null, "ERROR", "Internal error (serialization)"));
        }

        if (!hmacAuthService.isValidHmac(message, hmacHeader)) {
            log.warn("[SECURITY] Request failed HMAC validation (idempotencyKey={})", idempotencyKey);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new StatusResponse(null, "ERROR", "Invalid HMAC signature"));
        }

        try {
            StatusResponse response = logic.process(request, idempotencyKey);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[BUSINESS] Exception in business logic: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StatusResponse(null, "ERROR", e.getMessage()));
        }
    }

    /**
     * Functional interface for lambda business logic (for DRY HMAC check)
     */
    @FunctionalInterface
    private interface BusinessLogic {
        StatusResponse process(PaymentRequest request, String idempotencyKey);
    }
}
