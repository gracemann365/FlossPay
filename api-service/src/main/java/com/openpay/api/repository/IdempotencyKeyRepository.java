package com.openpay.api.repository;

import com.openpay.api.model.IdempotencyKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntity, String> {}
