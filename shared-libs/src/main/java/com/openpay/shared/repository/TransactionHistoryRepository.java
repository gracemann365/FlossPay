package com.openpay.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.shared.model.TransactionHistoryEntity;

/**
 * ====================================================================
 * TransactionHistoryRepository
 * --------------------------------------------------------------------
 * Repository interface for CRUD and query operations on
 * {@link TransactionHistoryEntity}.
 * <p>
 * Extends Spring Data JPA's {@link JpaRepository} to provide full access to
 * transaction history
 * records for auditing, traceability, and analytics.
 * </p>
 *
 * <h3>Usage:</h3>
 * <ul>
 * <li>Autowire this interface in services or components needing access to
 * transaction audit trails.</li>
 * <li>Supports all basic CRUD and custom finder methods as per Spring Data
 * conventions.</li>
 * <li>Add custom queries as needed for history lookups and reporting.</li>
 * </ul>
 *
 * <h3>Entity Mapping:</h3>
 * Maps to {@link TransactionHistoryEntity}, which stores immutable transaction
 * event snapshots.
 *
 * <h3>Thread Safety:</h3>
 * Interface is stateless and safe to use in singleton beans.
 *
 * @author David Grace
 * @since 1.0
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, Long> {
    // Extend with custom query methods for historical lookups if needed.
}
