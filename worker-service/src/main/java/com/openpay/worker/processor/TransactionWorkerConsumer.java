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
import com.openpay.worker.model.TransactionWorkerEntity;
import com.openpay.worker.repository.TransactionWorkerRepository;

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
/**
 * <h2>TransactionWorkerConsumer</h2>
 * <p>
 * Asynchronous processor for OpenPay worker-service. Consumes payment jobs from Redis Stream
 * ("transactions.main"), updates transaction records, and simulates network calls to UPI/NPCI.
 * </p>
 *
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *   <li>Poll Redis for queued transaction jobs (stream: transactions.main)</li>
 *   <li>Update transaction status ("processing", "completed", "failed") in DB</li>
 *   <li>Simulate UPI/NPCI network call via {@link NpciUpiGatewayClient}</li>
 *   <li>Handle all failures robustly, log every significant step</li>
 *   <li>Advance stream offset after each successful read</li>
 * </ul>
 *
 * <b>Extensible:</b> Designed for future retry, DLQ, metrics, tracing, and scaling.
 *
 * @author David Grace
 * @since 1.0
 */
@Component
public class TransactionWorkerConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionWorkerConsumer.class);

    private final RedisTemplate<Object, Object> redisWorkerTemplate;
    private final TransactionWorkerRepository transactionWorkerRepository;
    private final NpciUpiGatewayClient npciUpiGatewayClient;

    /**
     * Constructor: injects dependencies.
     * @param redisWorkerTemplate RedisTemplate for worker stream ops
     * @param transactionWorkerRepository Repository for transaction DB records
     * @param npciUpiGatewayClient Client to simulate UPI/NPCI payment gateway
     */
    public TransactionWorkerConsumer(
            RedisTemplate<Object, Object> redisWorkerTemplate,
            TransactionWorkerRepository transactionWorkerRepository,
            NpciUpiGatewayClient npciUpiGatewayClient) {

        this.redisWorkerTemplate = redisWorkerTemplate;
        this.transactionWorkerRepository = transactionWorkerRepository;
        this.npciUpiGatewayClient = npciUpiGatewayClient;
    }

    /**
     * CommandLineRunner bean: starts the forever-poll worker loop on service startup.
     */
    @Bean
    public CommandLineRunner streamListener() {
        return args -> {
            // Redis connectivity sanity check
            try {
                redisWorkerTemplate.opsForValue().set("service-check", "WORKER");
                Object val = redisWorkerTemplate.opsForValue().get("service-check");
                log.info("[WORKER] service-check key in Redis: {}", val);

                log.info("[WORKER] Redis connection info: {}",
                        redisWorkerTemplate.getConnectionFactory().getConnection().info("server"));
            } catch (Exception e) {
                log.error("[WORKER] Redis sanity check failed", e);
            }

            String stream = "transactions.main";
            String lastSeenId = "0-0"; // Start from beginning (for demo; production: use persisted offset)

            log.info("Starting to consume stream: {}", stream);

            while (true) {
                // Poll new messages from stream
                StreamOffset<Object> offset = StreamOffset.create(stream, ReadOffset.from(lastSeenId));
                log.info("About to poll Redis stream...");
                List<MapRecord<Object, Object, Object>> messages = redisWorkerTemplate.opsForStream().read(offset);

                log.info("Polled Redis stream, messages: {}", messages);

                if (messages != null && !messages.isEmpty()) {
                    for (MapRecord<Object, Object, Object> record : messages) {
                        log.info("🔥 Consumed message: ID={} Payload={}", record.getId(), record.getValue());
                        Map<Object, Object> payload = record.getValue();
                        Long txnId = null;
                        try {
                            Object txnIdObj = payload.get("txnId");
                            if (txnIdObj != null) {
                                txnId = txnIdObj instanceof Long
                                        ? (Long) txnIdObj
                                        : Long.valueOf(txnIdObj.toString());
                            } else {
                                log.error("txnId missing in stream payload: {}", payload);
                                continue;
                            }

                            // Find DB entity
                            TransactionWorkerEntity transactionWorkerEntity = transactionWorkerRepository.findById(txnId).orElse(null);
                            if (transactionWorkerEntity == null) {
                                log.error("Transaction not found in DB for txnId={}", txnId);
                                continue;
                            }

                            // Update to "processing"
                            transactionWorkerEntity.setStatus("processing");
                            transactionWorkerEntity.setUpdatedAt(LocalDateTime.now());
                            transactionWorkerRepository.save(transactionWorkerEntity);
                            log.info("Updated txnId={} to status=processing", txnId);

                            // Simulate UPI/NPCI call
                            boolean upiSuccess = npciUpiGatewayClient.initiateUpiPayment(
                                    transactionWorkerEntity.getSenderUpi(),
                                    transactionWorkerEntity.getReceiverUpi(),
                                    transactionWorkerEntity.getAmount(),
                                    txnId);

                            // Update to "completed" or "failed"
                            if (upiSuccess) {
                                transactionWorkerEntity.setStatus("completed");
                                log.info("Transaction {} completed via UPI", txnId);
                            } else {
                                transactionWorkerEntity.setStatus("failed");
                                log.warn("Transaction {} failed via UPI", txnId);
                            }
                            transactionWorkerEntity.setUpdatedAt(LocalDateTime.now());
                            transactionWorkerRepository.save(transactionWorkerEntity);

                        } catch (Exception e) {
                            log.error("Exception processing record: {}, error={}", record, e.getMessage(), e);
                        }

                        // Move the stream offset forward
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