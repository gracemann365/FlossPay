package com.openpay.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UpiValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpi {
    String message() default "Invalid UPI ID format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
