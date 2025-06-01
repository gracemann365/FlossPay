package com.openpay.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a payment transaction within the OpenPay system.
 * <p>
 * Contains key payment details including amount, sender and receiver UPI IDs,
 * transaction status, and timestamps for tracking lifecycle events.
 * </p>
 *
 * <h3>Database Mapping:</h3>
 * <ul>
 * <li>Table: <b>transactions</b></li>
 * <li>Primary key: {@code id}, auto-generated</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 * 
 *        earlier : TransactionEntity and transactionEntity
 *        post refactor : TransactionApiEntity and transactionApiEntity
 */
@Entity
@Table(name = "transactions")
public class TransactionApiEntity {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Monetary amount involved in the transaction.
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
     * Current status of the transaction (e.g., queued, processed, failed).
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

    // Getters and setters

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
