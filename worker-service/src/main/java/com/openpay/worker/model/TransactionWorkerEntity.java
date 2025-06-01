package com.openpay.worker.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <h2>TransactionWorkerEntity</h2>
 * <p>
 * Represents a payment transaction record in the worker-service context.
 * Mirrors the {@code transactions} table in the database.
 * <b>Do not use this entity outside worker-service; use corresponding API
 * entity there.</b>
 * </p>
 *
 * <ul>
 * <li>Table: {@code transactions}</li>
 * <li>Primary key: {@code id} (auto-increment, BIGSERIAL)</li>
 * <li>Includes status and timestamps for async job processing</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@Entity
@Table(name = "transactions")
public class TransactionWorkerEntity {

    /** Unique transaction identifier (DB PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Payment amount (Rupees and paise, precise to 2 decimals) */
    @Column(nullable = false)
    private BigDecimal amount;

    /** UPI ID of sender */
    @Column(nullable = false)
    private String senderUpi;

    /** UPI ID of receiver */
    @Column(nullable = false)
    private String receiverUpi;

    /** Transaction status (queued, processing, completed, failed, etc.) */
    @Column(nullable = false)
    private String status;

    /** Time when transaction was created */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Time when transaction status was last updated */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSenderUpi() {
        return senderUpi;
    }

    public void setSenderUpi(String senderUpi) {
        this.senderUpi = senderUpi;
    }

    public String getReceiverUpi() {
        return receiverUpi;
    }

    public void setReceiverUpi(String receiverUpi) {
        this.receiverUpi = receiverUpi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
