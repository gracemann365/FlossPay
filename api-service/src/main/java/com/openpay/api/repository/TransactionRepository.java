package com.openpay.api.repository;

import com.openpay.api.model.TransactionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {}
