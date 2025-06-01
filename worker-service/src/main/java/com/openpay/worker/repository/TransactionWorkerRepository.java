package com.openpay.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.worker.model.TransactionWorkerEntity; // <-- after renaming

/**
 * Repository for CRUD and query operations on worker-side transaction records.
 * <p>
 * Extends {@link JpaRepository} for full DB access to
 * {@link TransactionWorkerEntity}.
 * Used by worker-service for processing transaction status updates.
 * </p>
 *
 * <ul>
 * <li>Extend with custom worker-side queries if needed</li>
 * <li>Post-refactor: TransactionWorkerRepository (was
 * TransactionRepository)</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
public interface TransactionWorkerRepository extends JpaRepository<TransactionWorkerEntity, Long> {
}
// previously: TransactionRepository extends JpaRepository<TransactionEntity,
// Long>
