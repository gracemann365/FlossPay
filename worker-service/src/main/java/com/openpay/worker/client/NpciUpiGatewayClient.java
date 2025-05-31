/*
 * =============> Father what is my purpose ? <=======================
 * This NpciUpiGatewayClient class is your mock for what,
 * in real production, would be the bridge between your OpenPay gateway
 * (think: Razorpay, Cashfree, Paytm Payments Gateway, etc.)
 * and the actual UPI/NPCI backend switch.
 * 
 * ===========> Detailed Real-World Mapping:<===============================
 * ===>
 * Your API & Worker stack = OpenPay (i.e., your own payments gateway backend,
 * like Razorpay)
 * ===>
 * NpciUpiGatewayClient (your mock here) =
 * The “connector” code that, in a real production system:
 * 
 * Prepares a network call (could be HTTP, ISO 8583, or NPCI’s internal spec)
 * 
 * Hits the real NPCI UPI switch endpoint (the rails of Indian payments)
 * 
 * Handles responses: success, error, network failure, timeouts, etc.
 * ===>
 * NPCI/UPI switch =
 * The actual banking/UPI network that ultimately moves money between banks
 * (e.g., HDFC, SBI, Paytm, GPay, PhonePe, etc. all hit this).
 *
 */
package com.openpay.worker.client;

import java.math.BigDecimal;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Simulates interaction with the NPCI UPI gateway.
 * In real-world: replace with actual HTTP or ISO 8583 integration.
 * For MVP: randomly returns success or failure to test workflow.
 */
@Component
public class NpciUpiGatewayClient {
    private static final Logger log = LoggerFactory.getLogger(NpciUpiGatewayClient.class);
    private static final Random RANDOM = new Random();

    /**
     * Simulate sending a payment request to NPCI/UPI network.
     * 
     * @param senderUpi   Sender's UPI ID
     * @param receiverUpi Receiver's UPI ID
     * @param amount      Amount to transfer
     * @param txnId       Unique transaction ID
     * @return true if payment succeeded, false otherwise
     */
    public boolean initiateUpiPayment(String senderUpi, String receiverUpi, BigDecimal amount, Long txnId) {
        // Simulate network delay
        try {
            Thread.sleep(600); // 600ms to simulate network RTT
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Log payload as if calling a real UPI API
        log.info("[NPCI-UPI] Initiating payment: senderUpi={}, receiverUpi={}, amount={}, txnId={}",
                senderUpi, receiverUpi, amount, txnId);

        // Simulate random success/failure (80% success rate for realism)
        boolean success = RANDOM.nextInt(100) < 80;

        if (success) {
            log.info("[NPCI-UPI] Payment SUCCESS for txnId={}", txnId);
        } else {
            log.warn("[NPCI-UPI] Payment FAILURE for txnId={}", txnId);
        }
        return success;
    }
}
