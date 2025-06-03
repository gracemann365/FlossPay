package com.openpay.api.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ====================================================================
 * OpenPay UPI Gateway — Rate Limiter Filter (MVP)
 * --------------------------------------------------------------------
 * Production-grade request rate limiter (per client) using token-bucket logic.
 * Limits requests per client (header "X-Client-Id") to prevent abuse and
 * enforce fair usage.
 * --------------------------------------------------------------------
 * Audit/Compliance: All throttled events are logged with timestamp, client_id,
 * and endpoint.
 * 
 * How it works:
 * - Each request increments a counter for that client.
 * - Counter resets every windowMillis (default: 60 sec).
 * - If limit is exceeded: request is blocked with HTTP 429.
 * 
 * To switch to Redis later, just replace the in-memory map with a Redis store.
 * ====================================================================
 * <b>Note for Production (Bank-Grade) Upgrades:</b>
 * <ul>
 * <li>Replace in-memory map with a distributed, persistent store (e.g., Redis)
 * for cluster-wide quota enforcement.</li>
 * <li>Authenticate and identify clients using secure JWT tokens or OAuth2
 * scopes, not just headers.</li>
 * <li>Configure tiered, SLA-driven quotas and burst handling per
 * client/product/endpoint (supporting regulatory or contractual limits).</li>
 * <li>Make all rate changes/audits immutable—log events to append-only audit
 * ledgers (e.g., blockchain or tamper-proof DB).</li>
 * <li>Expose admin/ops APIs for live quota adjustment, customer support, and
 * incident response.</li>
 * <li>Integrate with observability stacks: real-time metrics, alerting, anomaly
 * detection for abuse/fraud prevention.</li>
 * </ul>
 * ====================================================================
 * 
 * @author David Grace
 * @since 1.0
 */
@Component
public class RateLimiterFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterFilter.class);

    // ======= Configurable Settings =======
    private static final int MAX_REQUESTS_PER_MINUTE = 10; // MVP: 10 requests/minute per client
    private static final long WINDOW_MILLIS = 60_000L; // 1 minute window

    /**
     * Tracks request counts and reset timestamps per client.
     * Key: clientId, Value: RequestCounter (count, resetTime)
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        // Debug log for every request
        log.warn("!!! RateLimiterFilter TRIGGERED for path={} clientId={} !!!",
                httpReq.getRequestURI(), httpReq.getHeader("X-Client-Id"));

        // Extract client identifier (MVP: header, e.g., "X-Client-Id")
        String clientId = httpReq.getHeader("X-Client-Id");
        if (clientId == null || clientId.isBlank()) {
            httpResp.setStatus(400);
            httpResp.getWriter().write("Missing X-Client-Id header");
            return;
        }

        // === Token-bucket logic, thread-safe ===
        boolean allowed = checkAndIncrementThreadSafe(clientId);

        if (!allowed) {
            // ===== Audit Blocked Event =====
            log.warn("[RATE LIMIT] Blocked request: client_id={}, path={}, ts={}",
                    clientId, httpReq.getRequestURI(), Instant.now());
            httpResp.setStatus(429);
            httpResp.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }

        // Allow the request to proceed
        chain.doFilter(request, response);
    }

    /**
     * Checks and increments the counter for a client in the current window.
     * If the window expired, resets the count.
     * This version is thread-safe for bursty traffic.
     * 
     * @param clientId
     * @return true if request is allowed; false if rate limit hit
     */
    private final Map<String, RequestCounter> clientRequestMap = new ConcurrentHashMap<>();
    private final Map<String, Object> clientLocks = new ConcurrentHashMap<>();

    private boolean checkAndIncrementThreadSafe(String clientId) {
        long now = System.currentTimeMillis();
        Object lock = clientLocks.computeIfAbsent(clientId, k -> new Object());
        boolean allowed;
        synchronized (lock) {
            RequestCounter counter = clientRequestMap.get(clientId);
            if (counter == null || now > counter.resetTime) {
                // New window, reset count
                counter = new RequestCounter(1, now + WINDOW_MILLIS);
                clientRequestMap.put(clientId, counter);
                allowed = true;
            } else if (counter.count < MAX_REQUESTS_PER_MINUTE) {
                counter.count++;
                allowed = true;
            } else {
                // Limit hit
                allowed = false;
            }
        }
        return allowed;
    }

    /**
     * Simple counter struct for per-client rate limiting.
     */
    private static class RequestCounter {
        int count;
        long resetTime;

        RequestCounter(int count, long resetTime) {
            this.count = count;
            this.resetTime = resetTime;
        }
    }
}
