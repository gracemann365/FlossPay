package com.openpay.shared.exception;

/**
 * <h2>OpenPayException</h2>
 * <p>
 * Base runtime exception for all custom, application-specific errors in the
 * OpenPay UPI Gateway.
 * </p>
 *
 * <p>
 * This exception serves as the parent for all OpenPay-related error conditions,
 * enabling unified error handling and consistent response formatting across the
 * codebase.
 * Subclasses (such as {@link InvalidUpiException}) can represent more specific
 * error types.
 * </p>
 *
 * <h3>Usage Example</h3>
 * 
 * <pre>{@code
 * throw new OpenPayException("Generic OpenPay system error");
 * }</pre>
 *
 * @author David Grace
 * @since 1.0
 * @see com.openpay.shared.exception.InvalidUpiException
 */
public class OpenPayException extends RuntimeException {

    /**
     * Constructs a new OpenPayException with no detail message.
     */
    public OpenPayException() {
        super();
    }

    /**
     * Constructs a new OpenPayException with the specified detail message.
     * 
     * @param message the detail message
     */
    public OpenPayException(String message) {
        super(message);
    }

    /**
     * Constructs a new OpenPayException with the specified detail message and
     * cause.
     * 
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public OpenPayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new OpenPayException with the specified cause.
     * 
     * @param cause the cause of this exception
     */
    public OpenPayException(Throwable cause) {
        super(cause);
    }
}
