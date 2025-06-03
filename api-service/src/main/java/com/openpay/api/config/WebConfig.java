// src/main/java/com/openpay/api/config/WebConfig.java
package com.openpay.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openpay.api.filter.RateLimiterFilter;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilterRegistration(
            RateLimiterFilter rateLimiterFilter) {
        FilterRegistrationBean<RateLimiterFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(rateLimiterFilter);
        registration.addUrlPatterns("/*"); // or "/pay" to limit scope
        registration.setOrder(1); // run early
        return registration;
    }
}
