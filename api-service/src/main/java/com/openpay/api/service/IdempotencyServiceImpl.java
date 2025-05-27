package com.openpay.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openpay.api.model.IdempotencyKeyEntity;
import com.openpay.api.repository.IdempotencyKeyRepository;

@Service
public class IdempotencyServiceImpl implements IdempotencyService {
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public IdempotencyServiceImpl(IdempotencyKeyRepository idempotencyKeyRepository) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Override
    public boolean isDuplicate(String key) {
        return idempotencyKeyRepository.existsByIdempotencyKey(key);
    }

    @Override
    @Transactional
    public void saveKey(String key, Long transactionId) {
        IdempotencyKeyEntity entity = new IdempotencyKeyEntity();
        entity.setIdempotencyKey(key);
        entity.setTransactionId(transactionId);
        entity.setCreatedAt(LocalDateTime.now());
        idempotencyKeyRepository.save(entity);
    }
}
