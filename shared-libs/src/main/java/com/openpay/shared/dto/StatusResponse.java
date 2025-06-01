package com.openpay.shared.dto;

/**
 * <h2>StatusResponse</h2>
 * <p>
 * Standard response DTO for returning the status and details of a payment
 * transaction
 * or any related API operation in the OpenPay UPI Gateway system.
 * </p>
 *
 * <p>
 * Used by the API layer to encapsulate and serialize transaction status,
 * unique transaction identifier, and any human-readable message
 * for client-facing feedback or debugging.
 * </p>
 *
 * <h3>Usage Example in Controller</h3>
 * 
 * <pre>{@code @GetMapping("/status/{id}")
 * public ResponseEntity<StatusResponse> getStatus( @PathVariable Long id) {
 *     Transaction txn = transactionService.getTransactionById(id);
 *     return ResponseEntity.ok(new StatusResponse(
 *             txn.getId(),
 *             txn.getStatus(),
 *             txn.getStatusMessage()));
 * }
 * }</pre>
 *
 * <ul>
 * <li>Typically serialized as JSON in API responses.</li>
 * <li>Includes optional message for errors, informational notes, etc.</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 * @see com.openpay.shared.dto.PaymentRequest
 */
public class StatusResponse {

    /**
     * Unique identifier for the transaction or resource.
     * <p>
     * Typically maps to the transaction ID in the database.
     */
    private Long id;

    /**
     * Current status of the transaction (e.g., "queued", "processing", "success",
     * "failed").
     */
    private String status;

    /**
     * Optional human-readable message providing additional context or error
     * information.
     */
    private String message;

    /**
     * Default constructor for serialization frameworks.
     */
    public StatusResponse() {
    }

    /**
     * Constructs a StatusResponse with transaction ID and status.
     *
     * @param id     the unique transaction ID
     * @param status the transaction status
     */
    public StatusResponse(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    /**
     * Constructs a StatusResponse with transaction ID, status, and additional
     * message.
     *
     * @param id      the unique transaction ID
     * @param status  the transaction status
     * @param message the additional message (optional)
     */
    public StatusResponse(Long id, String status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    /**
     * Gets the transaction/resource ID.
     * 
     * @return transaction ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the transaction/resource ID.
     * 
     * @param id transaction ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the current status of the transaction.
     * 
     * @return status (e.g., "success", "failed")
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the transaction.
     * 
     * @param status status (e.g., "success", "failed")
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the additional human-readable message.
     * 
     * @return message (may be null)
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the additional human-readable message.
     * 
     * @param message message (may be null)
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
