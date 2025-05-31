package com.openpay.worker.processor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.openpay.worker.client.NpciUpiGatewayClient;
import com.openpay.worker.model.TransactionEntity;
import com.openpay.worker.repository.TransactionRepository;

/**
 * ========================== OpenPay Transaction Worker Consumer ==========================
 *
 *  This class acts as the async background processor for queued UPI transactions.
 *  It reads payment requests from the Redis Stream ("transactions.main"), updates their status
 *  in the PostgreSQL DB, and simulates a network call to the UPI/NPCI gateway.
 *
 *  ====== Key Responsibilities ======
 *  - Polls Redis for new transactions (using reliable stream offset tracking)
 *  - Deserializes each message, extracts txnId & payload
 *  - Looks up the DB record and updates it to "processing"
 *  - Calls a mock UPI/NPCI client to simulate payment network
 *  - On success: marks as "completed"; on failure: marks as "failed"
 *  - Logs all steps for transparency and debugging
 *
 *  ====== Real-World Analogy ======
 *  - This is your "OpenPay worker" (think: Razorpay's backend job processor)
 *  - The NpciUpiGatewayClient is your "network connector" to the actual UPI/NPCI rails
 *
 *  ====== Designed for: ======
 *  - Extensibility: add retry, DLQ, metrics, tracing, etc.
 *  - Testability: logs every step, safely handles errors
 */
@Component
public class TransactionWorkerConsumer {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final TransactionRepository transactionRepository;
    private final NpciUpiGatewayClient npciUpiGatewayClient;

    /**
     * Constructor: injects dependencies  || try not use autowired my friend since its better for testing !
     */
    public TransactionWorkerConsumer(
            RedisTemplate<Object, Object> redisTemplate,
            TransactionRepository transactionRepository,
            NpciUpiGatewayClient npciUpiGatewayClient) {

        this.redisTemplate = redisTemplate;
        this.transactionRepository = transactionRepository;
        this.npciUpiGatewayClient = npciUpiGatewayClient;
    }

    private static final Logger log = LoggerFactory.getLogger(TransactionWorkerConsumer.class);

    /**
     * CommandLineRunner bean: runs on worker startup and processes stream jobs forever.
     *
     * - Sanity-checks Redis connection on launch
     * - Polls transactions.main for new jobs
     * - Processes each job, updates DB, simulates UPI result
     */
    @Bean
    public CommandLineRunner streamListener() {
        return args -> {
            // ========= Redis DB & Key Sanity Check =========
            try {
                // Set and read a test key to verify Redis connectivity
                redisTemplate.opsForValue().set("service-check", "WORKER");
                Object val = redisTemplate.opsForValue().get("service-check");
                log.info("[WORKER] service-check key in Redis: {}", val);

                // Print Redis server info (optional but useful)
                log.info("[WORKER] Redis connection info: {}",
                        redisTemplate.getConnectionFactory().getConnection().info("server"));
            } catch (Exception e) {
                log.error("[WORKER] Redis sanity check failed", e);
            }
            // ========= End Sanity Check =========

            String stream = "transactions.main";    // Main processing stream
            String lastSeenId = "0-0";              // Always start from the beginning for demo

            log.info("Starting to consume stream: {}", stream);

            // ==========> Polling Logic <===================
            while (true) {
                // Read all new messages from the stream (blocking/polling every 3 seconds)
                StreamOffset<Object> offset = StreamOffset.create(stream, ReadOffset.from(lastSeenId));
                log.info("About to poll Redis stream...");
                List<MapRecord<Object, Object, Object>> messages = redisTemplate.opsForStream().read(offset);

                log.info("Polled Redis stream, messages: {}", messages);

                if (messages != null && !messages.isEmpty()) {
                    for (MapRecord<Object, Object, Object> record : messages) {
                        // 1. Log and extract the payload
                        log.info("🔥 Consumed message: ID={} Payload={}", record.getId(), record.getValue());
                        Map<Object, Object> payload = record.getValue();
                        Long txnId = null;
                        try {
                            // 2. Extract txnId safely
                            Object txnIdObj = payload.get("txnId");
                            if (txnIdObj != null) {
                                txnId = txnIdObj instanceof Long
                                        ? (Long) txnIdObj
                                        : Long.valueOf(txnIdObj.toString());
                            } else {
                                log.error("txnId missing in stream payload: {}", payload);
                                continue;
                            }

                            // 3. Find DB entity
                            TransactionEntity txn = transactionRepository.findById(txnId).orElse(null);
                            if (txn == null) {
                                log.error("Transaction not found in DB for txnId={}", txnId);
                                continue;
                            }

                            // 4. Update to "processing" and timestamp
                            txn.setStatus("processing");
                            txn.setUpdatedAt(LocalDateTime.now());
                            transactionRepository.save(txn);
                            log.info("Updated txnId={} to status=processing", txnId);

                            // 5. Simulate NPCI/UPI network call
                            boolean upiSuccess = npciUpiGatewayClient.initiateUpiPayment(
                                    txn.getSenderUpi(),
                                    txn.getReceiverUpi(),
                                    txn.getAmount(),
                                    txnId);

                            // 6. Update to "completed" or "failed" based on network result
                            if (upiSuccess) {
                                txn.setStatus("completed");
                                log.info("Transaction {} completed via UPI", txnId);
                            } else {
                                txn.setStatus("failed");
                                log.warn("Transaction {} failed via UPI", txnId);
                            }
                            txn.setUpdatedAt(LocalDateTime.now());
                            transactionRepository.save(txn);

                        } catch (Exception e) {
                            log.error("Exception processing record: {}, error={}", record, e.getMessage(), e);
                        }

                        // 7. Move the stream offset forward
                        RecordId recordId = record.getId();
                        if (recordId != null) {
                            lastSeenId = recordId.getValue();
                        }
                    }
                } else {
                    log.debug("No new messages found in stream: {}", stream);
                }
                Thread.sleep(3000); // Poll every 3 seconds
            }
        };
    }
}
