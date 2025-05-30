package com.openpay.worker.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisStreamConsumer {

    private static final Logger log = LoggerFactory.getLogger(RedisStreamConsumer.class);

    @Bean
    public CommandLineRunner streamListener(RedisTemplate<String, Object> redisTemplate) {
        return args -> {
            String stream = "transactions.main";
            log.info("Starting to consume stream: {}", stream);

            while (true) {
                List<MapRecord<String, Object, Object>> messages =
                    redisTemplate.opsForStream().read(StreamOffset.fromStart(stream));
            
                if (messages != null && !messages.isEmpty()) {
                    for (MapRecord<String, Object, Object> record : messages) {
                        log.info("Consumed message from stream: {}", record.getValue());
                        // ...further processing...
                    }
                } else {
                    log.debug("No new messages found in stream: {}", stream);
                }
                Thread.sleep(3000);
            }
        };
    }
}
