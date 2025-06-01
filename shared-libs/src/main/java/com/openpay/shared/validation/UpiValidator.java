package com.openpay.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * <h2>UpiValidator</h2>
 * <p>
 * Jakarta Bean Validation (JSR-380) constraint validator for the
 * {@link ValidUpi} annotation.
 * Checks if a given String value conforms to a basic UPI ID format.
 * </p>
 *
 * <p>
 * This validator is automatically invoked by the validation framework
 * whenever a field annotated with {@link ValidUpi} is encountered in a DTO.
 * The current logic simply checks that the value is non-null and contains an
 * "@" symbol;
 * extend this for stricter UPI ID format rules as needed.
 * </p>
 *
 * <h3>Usage Example</h3>
 * 
 * <pre>{@code
 *  In PaymentRequest DTO: @ValidUpi
 * private String senderUpi;
 * }</pre>
 *
 * @author David Grace
 * @since 1.0
 * @see ValidUpi
 */
public class UpiValidator implements ConstraintValidator<ValidUpi, String> {

    /**
     * Checks if the provided UPI ID string is valid.
     *
     * @param upi     the UPI ID value to validate
     * @param context context for the validation (unused here)
     * @return {@code true} if the value is a valid UPI ID, {@code false} otherwise
     */
    @Override
    public boolean isValid(String upi, ConstraintValidatorContext context) {
        if (upi == null)
            return false;
        // TODO: Extend this logic for stricter UPI format validation
        return upi.contains("@"); // Basic check only
    }
}
