package com.openpay.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpiValidator implements ConstraintValidator<ValidUpi, String> {

    @Override
    public boolean isValid(String upi, ConstraintValidatorContext context) {
        if (upi == null)
            return false;
        return upi.contains("@"); // Stub: basic check only
    }
}
