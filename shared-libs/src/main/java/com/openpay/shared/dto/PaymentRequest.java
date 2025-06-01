package com.openpay.shared.dto;

import java.math.BigDecimal;

import com.openpay.shared.validation.ValidUpi;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * <h2>PaymentRequest</h2>
 * <p>
 * Data Transfer Object (DTO) representing an incoming payment initiation
 * request
 * for the OpenPay UPI Gateway API.
 * </p>
 *
 * <p>
 * This class is automatically populated by <b>Spring Boot</b> via the
 * <code>@RequestBody</code> annotation in your REST controller layer, which
 * binds the
 * JSON fields from an HTTP POST request into the corresponding fields of this
 * object.
 * </p>
 *
 * <h3>Example: Mapping Incoming JSON to PaymentRequest</h3>
 * 
 * <pre>
 * POST /pay
 * Content-Type: application/json
 * {
 *   "senderUpi":   "alice@upi",
 *   "receiverUpi": "bob@upi",
 *   "amount":      100.25
 * }
 * </pre>
 *
 * <h3>Usage Example in Controller</h3>
 * 
 * <pre>{@code @RestController
 * public class PaymentController { @PostMapping("/pay")
 *     public ResponseEntity<?> initiatePayment( @Valid @RequestBody PaymentRequest paymentRequest) {
 *         paymentService.process(paymentRequest);
 *         return ResponseEntity.ok().build();
 *     }
 * }
 * }</pre>
 *
 * <ul>
 * <li>Validation constraints ensure data integrity before business logic is
 * executed.</li>
 * <li>This DTO is typically mapped to a persistent transaction entity in the
 * service layer.</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 * @see com.openpay.shared.dto.StatusResponse
 */
public class PaymentRequest {

    /**
     * UPI ID of the sender who is initiating the payment.
     * <p>
     * Populated from JSON field <code>senderUpi</code> in the POST request body.
     * Must not be blank and must match UPI format.
     */
    @ValidUpi
    @NotBlank(message = "Sender UPI is required and must be a valid UPI ID")
    private String senderUpi;

    /**
     * UPI ID of the receiver who will receive the payment.
     * <p>
     * Populated from JSON field <code>receiverUpi</code> in the POST request body.
     * Must not be blank and must match UPI format.
     */
    @ValidUpi
    @NotBlank(message = "Receiver UPI is required and must be a valid UPI ID")
    private String receiverUpi;

    /**
     * Amount to be transferred in the transaction.
     * <p>
     * Populated from JSON field <code>amount</code> in the POST request body.
     * Must not be null and must be at least 0.01.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    // --- Getters & Setters ---

    /**
     * Gets the UPI ID of the sender.
     * 
     * @return sender's UPI ID (never blank)
     */
    public String getSenderUpi() {
        return senderUpi;
    }

    /**
     * Sets the UPI ID of the sender.
     * 
     * @param senderUpi sender's UPI ID (must be valid and not blank)
     */
    public void setSenderUpi(String senderUpi) {
        this.senderUpi = senderUpi;
    }

    /**
     * Gets the UPI ID of the receiver.
     * 
     * @return receiver's UPI ID (never blank)
     */
    public String getReceiverUpi() {
        return receiverUpi;
    }

    /**
     * Sets the UPI ID of the receiver.
     * 
     * @param receiverUpi receiver's UPI ID (must be valid and not blank)
     */
    public void setReceiverUpi(String receiverUpi) {
        this.receiverUpi = receiverUpi;
    }

    /**
     * Gets the payment amount.
     * 
     * @return transaction amount (minimum 0.01)
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the payment amount.
     * 
     * @param amount transaction amount (must be &gt; 0)
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
