package com.openpay.api.security;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

/**
 * ========================================================================
 * HmacAuthService: Utility for HMAC-SHA256 API authentication.
 * ------------------------------------------------------------------------
 * - Computes/validates HMAC signatures for request authentication.
 * - Supports constant-time comparison to prevent timing attacks.
 * - Centralizes cryptographic logic for clean controller separation.
 * ========================================================================
 * <b>IMPORTANT:</b> Store the secret securely (env/config) in production.
 * Never hardcode real keys in source!
 * ========================================================================
 * 
 * @author David Grace
 */
@Service
public class HmacAuthService {

    // For demo/local dev. For prod: inject via env/config/secrets!
    private static final String HMAC_SECRET = "super_secret_key_123";

    /**
     * Validates the HMAC of a message using the shared secret.
     *
     * @param message      The message (e.g., serialized body + idempotency-key)
     * @param providedHmac The HMAC received from client (Base64)
     * @return true if valid, false otherwise
     */
    public boolean isValidHmac(String message, String providedHmac) {
        String expected = hmacSha256(HMAC_SECRET, message);
        return constantTimeEquals(expected, providedHmac);
    }

    /**
     * Generates the HMAC SHA-256 signature of the message.
     */
    public String hmacSha256(String secret, String message) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(keySpec);
            byte[] hmacBytes = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute HMAC", e);
        }
    }

    /**
     * Compares two strings in constant time (defends against timing attacks).
     */
    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length())
            return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
