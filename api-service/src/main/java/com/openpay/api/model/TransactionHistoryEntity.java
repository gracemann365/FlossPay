package com.openpay.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing an immutable audit record for a status change in a
 * transaction's lifecycle.
 * <p>
 * Each record stores the previous and new status, change timestamp, and the
 * transaction it belongs to,
 * supporting compliance, debugging, and user-facing transaction history.
 * </p>
 *
 * <h3>Database Mapping:</h3>
 * <ul>
 * <li>Table: <b>transaction_history</b></li>
 * <li>Primary key: {@code history_id} (auto-generated)</li>
 * <li>Tracks all status transitions for any payment transaction</li>
 * </ul>
 *
 * @author (Your Name)
 * @since 1.0
 */
@Entity
@Table(name = "transaction_history")
public class TransactionHistoryEntity {

    /**
     * Unique identifier for this history event.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    /**
     * ID of the related transaction whose status changed.
     */
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    /**
     * Previous status before the change.
     */
    @Column(name = "prev_status", nullable = false)
    private String prevStatus;

    /**
     * New status after the change.
     */
    @Column(name = "new_status", nullable = false)
    private String newStatus;

    /**
     * Timestamp when the status change occurred.
     */
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    // Getters and Setters

    /** @return unique ID of this history record */
    public Long getHistoryId() {
        return historyId;
    }

    /** @param historyId unique ID to set */
    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    /** @return ID of the transaction associated with this history */
    public Long getTransactionId() {
        return transactionId;
    }

    /** @param transactionId ID of the related transaction */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /** @return previous status before change */
    public String getPrevStatus() {
        return prevStatus;
    }

    /** @param prevStatus previous status to set */
    public void setPrevStatus(String prevStatus) {
        this.prevStatus = prevStatus;
    }

    /** @return new status after change */
    public String getNewStatus() {
        return newStatus;
    }

    /** @param newStatus new status to set */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    /** @return timestamp when change occurred */
    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    /** @param changedAt change timestamp to set */
    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
