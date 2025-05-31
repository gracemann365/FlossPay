package com.openpay.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.worker.model.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> { }
