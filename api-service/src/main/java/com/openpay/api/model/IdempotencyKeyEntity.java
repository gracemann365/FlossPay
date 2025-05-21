package com.openpay.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKeyEntity {

    @Id
    private String key;

    private Long transactionId;

    private LocalDateTime createdAt;

    // Getters and Setters
}
