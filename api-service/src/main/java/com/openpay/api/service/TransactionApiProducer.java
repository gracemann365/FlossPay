package com.openpay.api.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.openpay.shared.dto.PaymentRequest;
import com.openpay.shared.exception.OpenPayException;
import com.openpay.shared.model.TransactionEntity;
import com.openpay.shared.repository.TransactionRepository;

import jakarta.annotation.PostConstruct;

/**
 * <h2>TransactionApiProducer</h2>
 * <p>
 * Service class responsible for orchestrating payment transaction creation,
 * ensuring idempotency, persisting transaction data, and publishing jobs to the
 * Redis stream.
 * </p>
 *
 * <ul>
 * <li>Handles business logic for the `/pay` endpoint (see
 * {@link com.openpay.api.controller.TransactionController})</li>
 * <li>Performs validation (e.g., sender ≠ receiver, idempotency key
 * uniqueness)</li>
 * <li>Persists transaction to the database and pushes payload to Redis for
 * async processing</li>
 * </ul>
 *
 * <b>How it works:</b>
 * <ol>
 * <li>Validates and persists the transaction</li>
 * <li>Checks and stores idempotency keys to avoid double-processing</li>
 * <li>Pushes a job to the Redis stream for the worker service to consume</li>
 * </ol>
 *
 * @author David Grace
 * @since 1.0
 */
@Service
public class TransactionApiProducer {

    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService;
    private static final Logger log = LoggerFactory.getLogger(TransactionApiProducer.class);
    private final RedisTemplate<Object, Object> redisApiTemplate;

    /**
     * Constructs the TransactionApiProducer with required dependencies via bean
     * injection.
     *
     * @param transactionRepository JPA repository for persisting transactions
     * @param idempotencyService    Service for idempotency key handling
     * @param redisApiTemplate      RedisTemplate for queueing jobs to stream
     */
    public TransactionApiProducer(TransactionRepository transactionRepository,
            IdempotencyService idempotencyService,
            RedisTemplate<Object, Object> redisApiTemplate) {
        this.transactionRepository = transactionRepository;
        this.idempotencyService = idempotencyService;
        this.redisApiTemplate = redisApiTemplate;
    }

    /**
     * Verifies API ↔ Redis connection on startup (debug/logging only).
     */
    @PostConstruct
    public void testApiRedis() {
        redisApiTemplate.opsForValue().set("service-check", "API");
        Object val = redisApiTemplate.opsForValue().get("service-check");
        log.info("[API] service-check key in Redis: {}", val);
    }

    /**
     * Logs which Redis connection factory (Lettuce/Jedis) and host:port is being
     * used.
     * For dev/local debugging.
     */
    @PostConstruct
    public void printRedisConnection() {
        RedisConnectionFactory factory = redisApiTemplate.getConnectionFactory();
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
        log.info("[API] Using Redis at: {}", hostPort);
    }

    /**
     * Main service method for creating and queuing a payment transaction.
     *
     * <p>
     * Steps:
     * <ol>
     * <li>Validates sender and receiver UPI IDs</li>
     * <li>Checks idempotency to prevent duplicate transactions</li>
     * <li>Persists the new transaction in the database</li>
     * <li>Stores the idempotency key (after successful DB save)</li>
     * <li>Builds a payload and pushes it to the Redis stream
     * (<code>transactions.main</code>)</li>
     * </ol>
     * </p>
     *
     * @param paymentRequestDto Payment details from the API
     * @param idempotencyKey    Unique client-supplied key for idempotency
     *                          enforcement
     * @return transaction ID of the newly created payment
     * @throws OpenPayException for business rule violations (e.g., duplicate key,
     *                          sender = receiver)
     */
    public Long createTransaction(PaymentRequest paymentRequestDto, String idempotencyKey) {
        if (paymentRequestDto.getSenderUpi().equalsIgnoreCase(paymentRequestDto.getReceiverUpi())) {
            throw new OpenPayException("Sender and receiver UPI must be different");
        }
        log.info("Creating transaction for sender={} receiver={}", paymentRequestDto.getSenderUpi(),
                paymentRequestDto.getReceiverUpi());

        // Idempotency check: throws if duplicate request is detected
        if (idempotencyService.isDuplicate(idempotencyKey)) {
            throw new OpenPayException("Duplicate request");
        }

        // Build and persist transaction entity
        TransactionEntity liveTransactionEntity = new TransactionEntity();
        liveTransactionEntity.setSenderUpi(paymentRequestDto.getSenderUpi());
        liveTransactionEntity.setReceiverUpi(paymentRequestDto.getReceiverUpi());
        liveTransactionEntity.setAmount(paymentRequestDto.getAmount());
        liveTransactionEntity.setStatus("queued");
        liveTransactionEntity.setCreatedAt(LocalDateTime.now());

        TransactionEntity savedTransactionEntity = transactionRepository.save(liveTransactionEntity);

        // Record idempotency key after successful save
        idempotencyService.saveKey(idempotencyKey, savedTransactionEntity.getId());

        // Prepare and push message to Redis Stream
        Map<Object, Object> streamPayload = new HashMap<>();
        streamPayload.put("txnId", savedTransactionEntity.getId());
        streamPayload.put("senderUpi", savedTransactionEntity.getSenderUpi());
        streamPayload.put("receiverUpi", savedTransactionEntity.getReceiverUpi());
        streamPayload.put("amount", savedTransactionEntity.getAmount().toString());

        redisApiTemplate.opsForValue().set("service-key", "hello-from-service"); // For dev/test only
        redisApiTemplate.opsForStream().add("transactions.main", streamPayload);
        log.info("Enqueued transaction {} to transactions.main stream", savedTransactionEntity.getId());

        return savedTransactionEntity.getId();
    }
}
