package com.openpay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openpay.api.model.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {}
