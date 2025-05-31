package com.openpay.api.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.openpay.api.model.TransactionEntity;
import com.openpay.api.repository.TransactionRepository;
import com.openpay.shared.dto.PaymentRequest;
import com.openpay.shared.exception.OpenPayException;

import jakarta.annotation.PostConstruct;
//this is your consumer for redis 

@Service
public class TransactionApiProducer {

    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService;
    private static final Logger log = LoggerFactory.getLogger(TransactionApiProducer.class);
    private final RedisTemplate<Object, Object> redisTemplate;

    public TransactionApiProducer(TransactionRepository transactionRepository, IdempotencyService idempotencyService,
            RedisTemplate<Object, Object> redisTemplate) {
        this.transactionRepository = transactionRepository;
        this.idempotencyService = idempotencyService;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void testApiRedis() {
        redisTemplate.opsForValue().set("service-check", "API");
        Object val = redisTemplate.opsForValue().get("service-check");
        log.info("[API] service-check key in Redis: " + val);
    }

    @PostConstruct
    public void printRedisConnection() {
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        String hostPort = "";
        if (factory instanceof org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory) {
            org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory lettuce = (org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory) factory;
            hostPort = lettuce.getHostName() + ":" + lettuce.getPort();
        } else if (factory instanceof org.springframework.data.redis.connection.jedis.JedisConnectionFactory) {
            org.springframework.data.redis.connection.jedis.JedisConnectionFactory jedis = (org.springframework.data.redis.connection.jedis.JedisConnectionFactory) factory;
            hostPort = jedis.getHostName() + ":" + jedis.getPort();
        } else {
            hostPort = "UnknownFactory: " + factory.getClass().getName();
        }
        log.info("[API] Using Redis at: " + hostPort);
    }

    public Long createTransaction(PaymentRequest request, String idempotencyKey) {
        if (request.getSenderUpi().equalsIgnoreCase(request.getReceiverUpi())) {
            throw new OpenPayException("Sender and receiver UPI must be different");
        }
        log.info("Creating transaction for sender={} receiver={}", request.getSenderUpi(), request.getReceiverUpi());
        // Idempotency check
        if (idempotencyService.isDuplicate(idempotencyKey)) {
            throw new OpenPayException("Duplicate request");
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setSenderUpi(request.getSenderUpi());
        entity.setReceiverUpi(request.getReceiverUpi());
        entity.setAmount(request.getAmount());
        entity.setStatus("queued");
        entity.setCreatedAt(LocalDateTime.now());

        // Store idempotency key after successful transaction save
        TransactionEntity saved = transactionRepository.save(entity);
        idempotencyService.saveKey(idempotencyKey, saved.getId());

        // YOU DONT need it return the id when using redis stream
        // return transactionRepository.save(entity).getId();
        // Build payload map for stream
        Map<Object, Object> streamPayload = new HashMap<>();
        streamPayload.put("txnId", saved.getId());
        streamPayload.put("senderUpi", saved.getSenderUpi());
        streamPayload.put("receiverUpi", saved.getReceiverUpi());
        streamPayload.put("amount", saved.getAmount().toString()); // BigDecimal as string

        // test for redis stream same instance
        redisTemplate.opsForValue().set("service-key", "hello-from-service");

        // Enqueue to Redis Stream
        redisTemplate.opsForStream().add("transactions.main", streamPayload);
        log.info("Enqueued transaction {} to transactions.main stream", saved.getId());

        return saved.getId();
    }
}
