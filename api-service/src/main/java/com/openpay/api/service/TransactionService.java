package com.openpay.api.service;

import com.openpay.api.dto.PaymentRequest;
import com.openpay.api.model.TransactionEntity;
import com.openpay.api.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Long createTransaction(PaymentRequest request) {
        if (request.getSenderUpi().equalsIgnoreCase(request.getReceiverUpi())) {
            throw new IllegalArgumentException("Sender and receiver UPI must be different");
        }

        // Stub: idempotency check will go here

        TransactionEntity entity = new TransactionEntity();
        entity.setSenderUpi(request.getSenderUpi());
        entity.setReceiverUpi(request.getReceiverUpi());
        entity.setAmount(request.getAmount());
        entity.setStatus("queued");
        entity.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(entity).getId();
    }
}
