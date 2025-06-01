package com.openpay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.api.model.TransactionApiEntity;

/**
 * Repository for CRUD/query operations on {@link TransactionApiEntity} used by the
 * API service.
 * <p>
 * Extends {@link JpaRepository} for full DB access to transaction records.
 * </p>
 * 
 * @author David Grace
 */
public interface TransactionApiRepository extends JpaRepository<TransactionApiEntity, Long> {
    // Extend with custom queries if needed.
}

// post refactor TransactionApiRepository & transactionApiRepository
// previously TransactionApiRepository & transactionApiRepository