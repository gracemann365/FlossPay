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

@RestController
@RequestMapping("/pay")
public class TransactionController {
private final TransactionApiProducer transactionApiProducer;

    public TransactionController(TransactionApiProducer transactionApiProducer) {
        this.transactionApiProducer = transactionApiProducer;
    }

    @PostMapping
    public ResponseEntity<StatusResponse> initiatePayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) { 

        Long id = transactionApiProducer.createTransaction(request, idempotencyKey); 
        StatusResponse response = new StatusResponse(id, "QUEUED", "Transaction queued");
        return ResponseEntity.ok(response);
    }
}
