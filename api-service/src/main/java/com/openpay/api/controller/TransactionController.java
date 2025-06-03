package com.openpay.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openpay.api.security.HmacAuthService;
import com.openpay.api.service.TransactionApiProducer;
import com.openpay.shared.dto.PaymentRequest;
import com.openpay.shared.dto.StatusResponse;

import jakarta.validation.Valid;

/**
 * ========================================================================
 * TransactionController: OpenPay /pay Endpoint (API-Hardening Edition)
 * ------------------------------------------------------------------------
 * - Initiates new UPI payment requests.
 * - Enforces API-level HMAC authentication for all payment requests.
 * - Validates request input, idempotency, and authenticates using HMAC.
 * - Delegates business logic to TransactionApiProducer (service layer).
 * ========================================================================
 * <b>API Endpoint:</b>
 * POST /pay
 * Headers: Idempotency-Key (required), X-HMAC (required)
 * Body: PaymentRequest
 * ========================================================================
 * <b>Security/Audit:</b>
 * - All /pay requests must include valid HMAC, or will be rejected (401/403).
 * - All failures are reported with appropriate HTTP status for traceability.
 * ========================================================================
 * 
 * @author David Grace
 * @since 1.0
 */
@RestController
@RequestMapping("/pay")
public class TransactionController {

    private final TransactionApiProducer transactionApiProducer;
    private final HmacAuthService hmacAuthService;

    public TransactionController(TransactionApiProducer transactionApiProducer,
            HmacAuthService hmacAuthService) {
        this.transactionApiProducer = transactionApiProducer;
        this.hmacAuthService = hmacAuthService;
    }

    /**
     * Handles payment initiation requests with HMAC authentication.
     *
     * @param request        Payment details (validated)
     * @param idempotencyKey Unique idempotency key (header)
     * @param hmacHeader     HMAC signature from client (header)
     * @return ResponseEntity with transaction status, ID, and audit message
     */
    @PostMapping
    public ResponseEntity<StatusResponse> initiatePayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader(value = "X-HMAC", required = false) String hmacHeader) {

        // ===== Audit: HMAC header missing =====
        if (hmacHeader == null || hmacHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(null, "ERROR", "Missing HMAC header"));
        }

        // ===== Audit: HMAC validation (use serialized request + key as message) =====
        String message = request.toString() + idempotencyKey;
        if (!hmacAuthService.isValidHmac(message, hmacHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new StatusResponse(null, "ERROR", "Invalid HMAC signature"));
        }

        // ===== Business logic: create and queue transaction =====
        Long id = transactionApiProducer.createTransaction(request, idempotencyKey);
        StatusResponse response = new StatusResponse(id, "QUEUED", "Transaction queued");
        return ResponseEntity.ok(response);
    }
}
