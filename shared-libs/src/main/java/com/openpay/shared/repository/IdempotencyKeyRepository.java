package com.openpay.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.shared.model.IdempotencyKeyEntity;

/**
 * ====================================================================
 * IdempotencyKeyRepository
 * --------------------------------------------------------------------
 * Repository interface for managing {@link IdempotencyKeyEntity} records.
 * <p>
 * Provides CRUD and custom query methods to handle idempotency key persistence,
 * preventing
 * duplicate transaction processing in the API service.
 * </p>
 *
 * <h3>Usage:</h3>
 * <ul>
 * <li>Autowire in services that need to store and check idempotency keys.</li>
 * <li>Custom query {@code existsByIdempotencyKey(String)} efficiently checks
 * for duplicates.</li>
 * </ul>
 *
 * <h3>Entity Mapping:</h3>
 * Maps to {@link IdempotencyKeyEntity}, storing unique keys for transaction
 * deduplication.
 *
 * @author David Grace
 * @since 1.0
 */
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntity, String> {

    /**
     * Checks if a given idempotency key exists in the data store.
     *
     * @param idempotencyKey the unique key to check
     * @return true if the key exists, false otherwise
     */
    boolean existsByIdempotencyKey(String idempotencyKey);
}
