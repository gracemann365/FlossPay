package com.openpay.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <h2>RedisApiConfig</h2>
 * <p>
 * Spring configuration for Redis integration in the API service.
 * Declares the primary {@link RedisTemplate} bean for (Object, Object)
 * serialization.
 * </p>
 *
 * <ul>
 * <li>Makes the (Object, Object) template the primary Redis bean for the API
 * module</li>
 * <li>Allows for plug-and-play with different RedisConnectionFactory
 * configurations</li>
 * <li>Can be extended to set custom serializers if required</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@Configuration
public class RedisApiConfig {

    /**
     * Configures the default {@link RedisTemplate} for (Object, Object) operations.
     * Marked as {@code @Primary} to override default template if multiple beans
     * exist.
     *
     * @param connectionFactory the Spring-managed Redis connection factory
     * @return the configured RedisTemplate instance
     */
    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplateObjectObject(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Optional: set default serializers if needed
        template.afterPropertiesSet();
        return template;
    }
}
