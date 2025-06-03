package com.openpay.shared.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ====================================================================
 * TransactionEntity (Shared: API + Worker)
 * --------------------------------------------------------------------
 * Canonical JPA entity mapping for the "transactions" table.
 * <p>
 * Used by both API-service and Worker-service for all payment flows,
 * including payment initiation, status updates, retries, and async processing.
 * </p>
 *
 * <h3>Database Mapping:</h3>
 * <ul>
 *   <li>Table: <b>transactions</b></li>
 *   <li>Primary key: {@code id} (auto-generated, BIGSERIAL)</li>
 *   <li>Schema: Amount, sender/receiver UPI, status, timestamps</li>
 * </ul>
 *
 * <h3>Compliance & OSS Best Practice:</h3>
 * <ul>
 *   <li>Single source of truth: Used by all microservices to avoid drift/bugs</li>
 *   <li>Do not duplicate or subclass this entity outside shared-libs</li>
 *   <li>Supports PCI-DSS, BFSI, SOX, OSS-compliant audits</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    /**
     * Unique identifier for the transaction (DB PK, auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Monetary amount involved in the transaction.
     * (Rupees and paise, precise to 2 decimals)
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * UPI ID of the sender initiating the transaction.
     */
    @Column(nullable = false)
    private String senderUpi;

    /**
     * UPI ID of the receiver.
     */
    @Column(nullable = false)
    private String receiverUpi;

    /**
     * Current status of the transaction (e.g., queued, processing, completed, failed).
     */
    @Column(nullable = false)
    private String status;

    /**
     * Timestamp when the transaction was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the transaction was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---

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
