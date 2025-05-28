package com.openpay.api.service;

public interface IdempotencyService {
    boolean isDuplicate(String key);
    void saveKey(String key, Long transactionId);
}
