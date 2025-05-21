package com.openpay.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openpay.api.dto.PaymentRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pay")
public class TransactionController {

    @PostMapping
    public ResponseEntity<String> initiatePayment(@Valid @RequestBody PaymentRequest request) {
    return ResponseEntity.ok("Transaction queued for " + request.getSenderUpi());
}

}
