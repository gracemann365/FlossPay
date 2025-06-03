package com.openpay.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.shared.model.TransactionEntity;

/**
 * ====================================================================
 * TransactionRepository (Shared: API + Worker)
 * --------------------------------------------------------------------
 * Canonical JPA repository interface for the "transactions" table.
 * <p>
 * Used by both API-service and Worker-service for all CRUD and query
 * operations on payment transaction records.
 * </p>
 *
 * <ul>
 * <li>Extends {@link JpaRepository} for full DB access</li>
 * <li>Add custom queries here as needed for either service</li>
 * </ul>
 *
 * <h3>Compliance & OSS Best Practice:</h3>
 * <ul>
 * <li>Single source of truth for transaction data access</li>
 * <li>Supports DRY, PCI-DSS, BFSI, SOX, OSS-compliant audit flows</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    // Extend with custom queries as needed (for API, worker, or shared use)
}
