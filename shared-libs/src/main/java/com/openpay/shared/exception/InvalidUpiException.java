package com.openpay.shared.exception;

/**
 * <h2>InvalidUpiException</h2>
 * <p>
 * Exception thrown when a provided UPI ID fails validation or does not meet
 * the format requirements within the OpenPay UPI Gateway.
 * </p>
 *
 * <p>
 * Typically used in validation logic (service or controller layer) to indicate
 * that an input UPI ID is syntactically or semantically invalid.
 * This exception is a specialized form of {@link OpenPayException}.
 * </p>
 *
 * <h3>Usage Example</h3>
 * 
 * <pre>{@code
 * if (!UpiValidator.isValid(upiId)) {
 *     throw new InvalidUpiException("Invalid UPI ID format: " + upiId);
 * }
 * }</pre>
 *
 * @author David Grace
 * @since 1.0
 * @see com.openpay.shared.exception.OpenPayException
 */
public class InvalidUpiException extends OpenPayException {
    /**
     * Constructs a new InvalidUpiException with a detailed error message.
     *
     * @param message explanation of the validation failure
     */
    public InvalidUpiException(String message) {
        super(message);
    }
}
