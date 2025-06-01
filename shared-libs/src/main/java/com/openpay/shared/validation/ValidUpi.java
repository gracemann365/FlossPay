package com.openpay.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * <h2>ValidUpi</h2>
 * <p>
 * Custom validation annotation for verifying that a field contains a
 * syntactically valid UPI ID.
 * </p>
 *
 * <p>
 * This annotation can be applied to any String field (typically in DTOs)
 * to enforce UPI format validation automatically via Jakarta Bean Validation
 * (JSR-380).
 * It delegates the actual validation logic to {@link UpiValidator}.
 * </p>
 *
 * <h3>Usage Example</h3>
 * 
 * <pre>{@code
 * public class PaymentRequest { @ValidUpi
 *     private String senderUpi;
 * }
 * }</pre>
 *
 * <ul>
 * <li>The default error message can be customized per field.</li>
 * <li>Useful for ensuring data integrity at the API boundary.</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 * @see UpiValidator
 */
@Documented
@Constraint(validatedBy = UpiValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpi {
    /**
     * The error message to return when validation fails.
     * 
     * @return error message
     */
    String message() default "Invalid UPI ID format";

    /**
     * Validation groups (for advanced validation scenarios).
     * 
     * @return validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload for clients to specify validation severity or category.
     * 
     * @return payload type
     */
    Class<? extends Payload>[] payload() default {};
}
