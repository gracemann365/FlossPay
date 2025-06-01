package com.openpay.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openpay.api.service.TransactionApiProducer;
import com.openpay.shared.dto.PaymentRequest;
import com.openpay.shared.dto.StatusResponse;

import jakarta.validation.Valid;

/**
 * <h2>TransactionController</h2>
 * <p>
 * REST controller for handling UPI payment initiation requests in the OpenPay
 * API.
 * Exposes the public `/pay` endpoint for clients to create new transactions.
 * </p>
 *
 * <p>
 * Validates all incoming requests, ensures idempotency, and delegates business
 * logic to the service layer.
 * Returns a {@link StatusResponse} with transaction ID and queue status.
 * </p>
 *
 * <h3>Endpoint</h3>
 * <ul>
 * <li><b>POST /pay</b> — Initiate a UPI transaction</li>
 * </ul>
 *
 * <h3>Example Request</h3>
 * 
 * <pre>
 * POST /pay
 * Headers: Idempotency-Key: some-unique-key
 * Body:
 * {
 *   "senderUpi": "alice@upi",
 *   "receiverUpi": "bob@upi",
 *   "amount": 100.25
 * }
 * </pre>
 *
 * <h3>Example Response</h3>
 * 
 * <pre>
 * {
 *   "id": 42,
 *   "status": "QUEUED",
 *   "message": "Transaction queued"
 * }
 * </pre>
 *
 * @author David Grace
 * @since 1.0
 * @see com.openpay.shared.dto.PaymentRequest
 * @see com.openpay.shared.dto.StatusResponse
 */
@RestController
@RequestMapping("/pay")
public class TransactionController {

    private final TransactionApiProducer transactionApiProducer;

    /**
     * Constructs the controller with required dependencies.
     * 
     * @param transactionApiProducer the service that handles payment creation logic
     */
    public TransactionController(TransactionApiProducer transactionApiProducer) {
        this.transactionApiProducer = transactionApiProducer;
    }

    /**
     * Handles payment initiation requests.
     *
     * @param request        Payment details (validated via {@link PaymentRequest})
     * @param idempotencyKey Unique key to ensure the operation is idempotent
     * @return Response containing transaction ID and status
     */
    @PostMapping
    public ResponseEntity<StatusResponse> initiatePayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        // Passes request to the service layer, which creates the transaction and
        // enqueues for processing
        Long id = transactionApiProducer.createTransaction(request, idempotencyKey);

        // Build and return API response
        StatusResponse response = new StatusResponse(id, "QUEUED", "Transaction queued");
        return ResponseEntity.ok(response);
    }
}
