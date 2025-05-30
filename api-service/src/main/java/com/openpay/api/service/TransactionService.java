package com.openpay.api.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.openpay.api.model.TransactionEntity;
import com.openpay.api.repository.TransactionRepository;
import com.openpay.shared.dto.PaymentRequest;
import com.openpay.shared.exception.OpenPayException;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public TransactionService(TransactionRepository transactionRepository, IdempotencyService idempotencyService,
            RedisTemplate<String, Object> redisTemplate) {
        this.transactionRepository = transactionRepository;
        this.idempotencyService = idempotencyService;
        this.redisTemplate = redisTemplate;
    }

    public Long createTransaction(PaymentRequest request, String idempotencyKey) {
        if (request.getSenderUpi().equalsIgnoreCase(request.getReceiverUpi())) {
            throw new OpenPayException("Sender and receiver UPI must be different");
        }
        log.info("Creating transaction for sender={} receiver={}", request.getSenderUpi(), request.getReceiverUpi());
        // Idempotency check
        if (idempotencyService.isDuplicate(idempotencyKey)) {
            throw new OpenPayException("Duplicate request");
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setSenderUpi(request.getSenderUpi());
        entity.setReceiverUpi(request.getReceiverUpi());
        entity.setAmount(request.getAmount());
        entity.setStatus("queued");
        entity.setCreatedAt(LocalDateTime.now());

        // Store idempotency key after successful transaction save
        TransactionEntity saved = transactionRepository.save(entity);
        idempotencyService.saveKey(idempotencyKey, saved.getId());

        // YOU DONT need it return the id when using redis stream
        //return transactionRepository.save(entity).getId();
        // Build payload map for stream
        Map<String, Object> streamPayload = new HashMap<>();
        streamPayload.put("txnId", saved.getId());
        streamPayload.put("senderUpi", saved.getSenderUpi());
        streamPayload.put("receiverUpi", saved.getReceiverUpi());
        streamPayload.put("amount", saved.getAmount().toString()); // BigDecimal as string

        // Enqueue to Redis Stream
        redisTemplate.opsForStream().add("transactions.main", streamPayload);
        log.info("Enqueued transaction {} to transactions.main stream", saved.getId());

        return saved.getId();
    }
}
