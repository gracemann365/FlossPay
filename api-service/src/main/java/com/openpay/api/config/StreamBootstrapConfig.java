package com.openpay.api.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class StreamBootstrapConfig {
    private static final Logger log = LoggerFactory.getLogger(StreamBootstrapConfig.class);

    @Bean
    public CommandLineRunner redisStreamBootstrap(RedisTemplate<Object, Object> redisTemplate) {
        return args -> {
            Map<Object, Object> fields = new HashMap<>();
            fields.put("test", "init");
            redisTemplate.opsForStream().add("transactions.main", fields);
            redisTemplate.opsForStream().add("transactions.retry", fields);
            redisTemplate.opsForStream().add("transactions.dlq", fields);
            log.info(
                    "==============>✅ Redis Streams created/verified: transactions.main, transactions.retry, transactions.dlq");

        };
    }
}
