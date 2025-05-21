package com.openpay.api.repository;

import com.openpay.api.model.TransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, Long> {}
