package com.openpay.worker.processor;

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

import com.openpay.worker.model.TransactionEntity;
import com.openpay.worker.repository.TransactionRepository;

@Component
public class TransactionWorkerConsumer {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final TransactionRepository transactionRepository;  

    public TransactionWorkerConsumer(
            RedisTemplate<Object, Object> redisTemplate,
            TransactionRepository transactionRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.transactionRepository = transactionRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(TransactionWorkerConsumer.class);

    @Bean
    public CommandLineRunner streamListener() {
        return args -> {
            // ======= Redis DB & Key Sanity Check =======
            try {
                // Set and read a test key to verify Redis connectivity
                redisTemplate.opsForValue().set("service-check", "WORKER");
                Object val = redisTemplate.opsForValue().get("service-check");
                log.info("[WORKER] service-check key in Redis: " + val);

                // Print Redis server info (optional but useful)
                log.info("[WORKER] Redis connection info: " +
                    redisTemplate.getConnectionFactory().getConnection().info("server"));
            } catch (Exception e) {
                log.error("[WORKER] Redis sanity check failed", e);
            }
            // ======= End Sanity Check =======

            String stream = "transactions.main";
            String lastSeenId = "0-0";

            log.info("Starting to consume stream: {}", stream);

            // Optional print connection info on console
            System.out.println("Redis connection info: " +
                    redisTemplate.getConnectionFactory().getConnection().info("server"));

            // ==========> Polling Logic <===================
            while (true) {
                StreamOffset<Object> offset = StreamOffset.create(stream, ReadOffset.from(lastSeenId));
                log.info("About to poll Redis stream...");
                List<MapRecord<Object, Object, Object>> messages = redisTemplate.opsForStream().read(offset);

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

                            TransactionEntity txn = transactionRepository.findById(txnId).orElse(null);
                            if (txn == null) {
                                log.error("Transaction not found in DB for txnId={}", txnId);
                                continue;
                            }

                            txn.setStatus("processing");
                            txn.setUpdatedAt(java.time.LocalDateTime.now());
                            transactionRepository.save(txn);
                            log.info("Updated txnId={} to status=processing", txnId);

                            // Add your UPI simulation & final status update logic here

                        } catch (Exception e) {
                            log.error("Exception processing record: {}, error={}", record, e.getMessage(), e);
                        }

                        RecordId recordId = record.getId();
                        if (recordId != null) {
                            lastSeenId = recordId.getValue();
                        }
                    }
                } else {
                    log.debug("No new messages found in stream: {}", stream);
                }
                Thread.sleep(3000);
            }
        };
    }
}
