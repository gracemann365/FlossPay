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

/**
 * <h2>LoggingFilter</h2>
 * <p>
 * Servlet filter that attaches a unique request ID to each API request via
 * SLF4J MDC.
 * Ensures that all logs within the request flow include a unique identifier for
 * tracing.
 * </p>
 * MDC, or Mapped Diagnostic Context, is a feature in SLF4J (Simple Logging
 * Facade for Java) that allows you to add contextual information to log
 * messages. This information is stored in a thread-local map and can be
 * accessed by logging frameworks like Logback and Log4j
 * <ul>
 * <li>Generates a new UUID per request as {@code requestId}</li>
 * <li>Puts the ID in MDC so it's available in log patterns and log output</li>
 * <li>Always clears MDC after the request to prevent memory leaks</li>
 * </ul>
 *
 * <h3>Usage:</h3>
 * <ul>
 * <li>Component is auto-registered as a filter in Spring Boot apps</li>
 * <li>Requires your logback/log4j config to reference {@code %X{requestId}} for
 * log output</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Generate a unique request ID (can also use header if present)
        String requestId = UUID.randomUUID().toString();

        // Put it into MDC (Mapped Diagnostic Context)
        MDC.put("requestId", requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Always clear MDC after the request to prevent memory leaks
            MDC.clear();
        }
    }
}
