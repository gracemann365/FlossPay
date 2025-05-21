package com.openpay.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openpay.api.dto.PaymentRequest;
import com.openpay.api.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pay")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        Long id = transactionService.createTransaction(request);
        return ResponseEntity.ok("Transaction queued with ID: " + id);
    }
}
