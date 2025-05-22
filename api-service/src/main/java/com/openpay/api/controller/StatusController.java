package com.openpay.api.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openpay.api.model.TransactionEntity;
import com.openpay.api.repository.TransactionRepository;

// *** THIS IS THE ONLY CHANGE NEEDED: ***
import com.openpay.shared.dto.StatusResponse;

@RestController
@RequestMapping("/transaction")
public class StatusController {

    private final TransactionRepository transactionRepository;

    public StatusController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable("id") Long id) {
        Optional<TransactionEntity> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // *** NO CHANGE: just use the shared StatusResponse ***
        return ResponseEntity.ok(
                new StatusResponse(id, transaction.get().getStatus()));
    }
    // *** REMOVED THE INNER CLASS ***
}
