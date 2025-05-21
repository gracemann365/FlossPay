package com.openpay.api.controller;

import com.openpay.api.model.TransactionEntity;
import com.openpay.api.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class StatusController {

    private final TransactionRepository transactionRepository;

    public StatusController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable Long id) {
        Optional<TransactionEntity> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new StatusResponse(id, transaction.get().getStatus()));
    }

    static class StatusResponse {
        private Long id;
        private String status;

        public StatusResponse(Long id, String status) {
            this.id = id;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }
    }
}
