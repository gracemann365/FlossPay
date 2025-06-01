package com.openpay.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h2>WorkerApplication</h2>
 * <p>
 * Entry point for the OpenPay worker-service Spring Boot application.
 * Boots the async transaction processor, initializes all beans/configs.
 * </p>
 *
 * <ul>
 * <li>Scans for all worker-specific components/configs</li>
 * <li>Should be launched as a background/job service, not as a web API</li>
 * </ul>
 *
 * <h3>Usage:</h3>
 * 
 * <pre>
 *   $ java -jar worker-service.jar
 * </pre>
 *
 * @author David Grace
 * @since 1.0
 */
@SpringBootApplication
public class WorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
