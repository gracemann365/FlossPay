package com.openpay.api.service;

/**
 * Service interface for idempotency key management in transaction workflows.
 * <p>
 * Ensures that operations identified by a unique key are processed at most
 * once,
 * providing protection against duplicate processing in distributed systems or
 * network retries.
 * </p>
 * 
 * <p>
 * Typical usage: before processing a payment, check
 * {@link #isDuplicate(String)}
 * with the request idempotency key. If not duplicate, process and call
 * {@link #saveKey(String, Long)} to register the key/transaction mapping.
 * </p>
 * 
 * <strong>Implementations must ensure thread safety and durability as
 * appropriate
 * to system guarantees.</strong>
 *
 * @author (Your Name)
 * @since 1.0
 */
public interface IdempotencyService {

    /**
     * Checks if the provided idempotency key has already been used for a previous
     * operation.
     *
     * @param key the unique idempotency key associated with the incoming request or
     *            transaction
     * @return true if the key was previously used (duplicate request), false
     *         otherwise
     */
    boolean isDuplicate(String key);

    /**
     * Records a new idempotency key along with the associated transaction ID.
     * <p>
     * This should be called after successfully processing a new, unique operation,
     * so that future requests with the same key can be flagged as duplicates.
     * </p>
     *
     * @param key           the unique idempotency key to register
     * @param transactionId the transaction ID generated for this operation
     */
    void saveKey(String key, Long transactionId);
}
