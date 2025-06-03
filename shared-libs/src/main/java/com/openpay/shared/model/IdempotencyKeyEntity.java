package com.openpay.shared.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ====================================================================
 * IdempotencyKeyEntity
 * --------------------------------------------------------------------
 * Entity representing a persisted idempotency key record for deduplication of
 * API requests.
 * <p>
 * Used to ensure that requests with the same external key are processed only
 * once,
 * preventing duplicate transactions in distributed or retry scenarios.
 * </p>
 *
 * <h3>Database Mapping:</h3>
 * <ul>
 * <li>Table: <b>idempotency_keys</b></li>
 * <li>Primary key: {@code idempotency_key}</li>
 * <li>Associates the key with a transaction and creation timestamp</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKeyEntity {

    /**
     * Unique idempotency key for the request/operation.
     * Primary key for the entity.
     */
    @Id
    @Column(name = "idempotency_key")
    private String idempotencyKey;

    /**
     * The ID of the transaction associated with this idempotency key.
     * Required for deduplication and traceability.
     */
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    /**
     * The timestamp when the idempotency key was created/stored.
     * Useful for expiry and audit purposes.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters

    /**
     * Gets the idempotency key.
     * 
     * @return the idempotency key string
     */
    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    /**
     * Sets the idempotency key.
     * 
     * @param idempotencyKey the unique key to set
     */
    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    /**
     * Gets the associated transaction ID.
     * 
     * @return the transaction ID
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the transaction ID for this key.
     * 
     * @param transactionId the transaction ID to associate
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Gets the creation timestamp of this idempotency key.
     * 
     * @return the creation time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * 
     * @param createdAt the time this record was created
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
