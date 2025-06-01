package com.openpay.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openpay.api.model.IdempotencyKeyEntity;
import com.openpay.api.repository.IdempotencyKeyRepository;

/**
 * Implementation of {@link IdempotencyService} that persists idempotency keys
 * in a backing data store.
 * <p>
 * This implementation uses a JPA repository to store each idempotency key along
 * with the associated transaction ID
 * and creation timestamp. Ensures duplicate requests can be detected and new
 * keys registered atomically.
 * </p>
 *
 * <strong>Thread-safe, production-grade, and compatible with transactional
 * guarantees.</strong>
 *
 * <h3>Design Notes:</h3>
 * <ul>
 * <li>All idempotency logic is persisted in the database via
 * {@link IdempotencyKeyRepository}.</li>
 * <li>Transactional annotation ensures atomicity for key registration.</li>
 * <li>Use case: prevent re-processing of payments or actions with same external
 * request ID.</li>
 * </ul>
 *
 * @author (Your Name)
 * @since 1.0
 */
@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    /**
     * Constructs a new IdempotencyServiceImpl with the given repository.
     *
     * @param idempotencyKeyRepository the JPA repository for idempotency key
     *                                 entities
     */
    public IdempotencyServiceImpl(IdempotencyKeyRepository idempotencyKeyRepository) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    /**
     * Checks if the provided idempotency key already exists in the system.
     * <p>
     * Used to determine if the incoming request is a duplicate and should be
     * ignored.
     * </p>
     *
     * @param key the unique idempotency key from the client/request
     * @return true if the key already exists (duplicate), false otherwise
     */
    @Override
    public boolean isDuplicate(String key) {
        // Query repository for existence of the key (indexed column)
        return idempotencyKeyRepository.existsByIdempotencyKey(key);
    }

    /**
     * Registers a new idempotency key and associates it with a transaction ID.
     * <p>
     * The method is transactional to ensure atomic insertion. Also timestamps the
     * key for audit/debug.
     * </p>
     *
     * @param key           the unique idempotency key to store
     * @param transactionId the transaction ID to associate with this key
     */
    @Override
    @Transactional
    public void saveKey(String key, Long transactionId) {
        // Create a new entity to store the idempotency key and transaction mapping
        IdempotencyKeyEntity entity = new IdempotencyKeyEntity();
        entity.setIdempotencyKey(key);
        entity.setTransactionId(transactionId);
        entity.setCreatedAt(LocalDateTime.now()); // Record time of registration

        // Persist the entity using the repository
        idempotencyKeyRepository.save(entity);
    }
}
