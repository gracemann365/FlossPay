package com.openpay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.api.model.IdempotencyKeyEntity;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntity, String> {
    boolean existsByIdempotencyKey(String idempotencyKey);
}
