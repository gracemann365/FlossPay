package com.openpay.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h2>HealthController</h2>
 * <p>
 * Provides a lightweight health check endpoint for the API service.
 * Useful for load balancers, uptime monitors, and orchestration systems
 * to verify that the service is running and responsive.
 * </p>
 *
 * <h3>Endpoint</h3>
 * <ul>
 * <li><b>GET /health</b> — Returns service health status ("UP")</li>
 * </ul>
 *
 * <h3>Example Response</h3>
 * 
 * <pre>
 * UP
 * </pre>
 *
 * <ul>
 * <li>For extensibility, consider returning JSON or including DB/Redis checks
 * in the future.</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 */
@RestController
public class HealthController {

    /**
     * Health check endpoint.
     *
     * @return plain text "UP" if the service is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        // In production, extend this to check DB, Redis, etc.
        return ResponseEntity.ok("liveness Check : Im Alive");
    }

    @GetMapping("/health/ready")
    public ResponseEntity<String> ready() {
        // MVP: Always READY. Add DB/Redis checks later if needed.
        return ResponseEntity.ok("READY");
    }
}
