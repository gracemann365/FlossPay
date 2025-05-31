package com.openpay.worker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplateObjectObject(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();

        // Add this debug block
        if (connectionFactory instanceof org.springframework.data.redis.connection.jedis.JedisConnectionFactory) {
            org.springframework.data.redis.connection.jedis.JedisConnectionFactory jedis = (org.springframework.data.redis.connection.jedis.JedisConnectionFactory) connectionFactory;
            System.out.println("### [DEBUG] Redis host=" + jedis.getHostName() +
                    ", port=" + jedis.getPort() +
                    ", db=" + jedis.getDatabase());
        }
        return template;
    }
}