package com.openpay.api.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <h2>StreamBootstrapConfig</h2>
 * <p>
 * Spring configuration class for initializing Redis streams required by the
 * OpenPay payment flow.
 * On application startup, ensures all necessary streams exist in Redis.
 * </p>
 *
 * <ul>
 * <li>Creates or verifies existence of {@code transactions.main},
 * {@code transactions.retry}, and {@code transactions.dlq} streams</li>
 * <li>Runs automatically at startup via {@link CommandLineRunner}</li>
 * <li>Uses the provided RedisTemplate for stream operations</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@Configuration
public class StreamBootstrapConfig {
    private static final Logger log = LoggerFactory.getLogger(StreamBootstrapConfig.class);

    /**
     * Bootstraps all required Redis streams at application startup.
     * Adds a test/init field to each stream to ensure existence.
     *
     * @param redisApiStreamBootStrapTemplate configured RedisTemplate for stream
     *                                        operations
     * @return CommandLineRunner that executes on application startup
     */
    @Bean
    public CommandLineRunner redisStreamBootstrap(RedisTemplate<Object, Object> redisApiStreamBootStrapTemplate) {
        return args -> {
            Map<Object, Object> fields = new HashMap<>();
            fields.put("test", "init");
            redisApiStreamBootStrapTemplate.opsForStream().add("transactions.main", fields);
            redisApiStreamBootStrapTemplate.opsForStream().add("transactions.retry", fields);
            redisApiStreamBootStrapTemplate.opsForStream().add("transactions.dlq", fields);
            log.info(
                    "==============>✅ Redis Streams created/verified: transactions.main, transactions.retry, transactions.dlq");
        };
    }
}
