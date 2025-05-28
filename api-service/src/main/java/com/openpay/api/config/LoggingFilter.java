package com.openpay.api.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Generate a unique request ID (can also use header if present)
        String requestId = UUID.randomUUID().toString();

        // Put it into MDC
        MDC.put("requestId", requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Always clear MDC after the request to prevent memory leaks
            MDC.clear();
        }
    }
}
