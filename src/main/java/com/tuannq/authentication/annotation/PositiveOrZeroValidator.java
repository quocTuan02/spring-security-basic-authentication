package com.tuannq.authentication.annotation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PositiveOrZeroValidator implements ConstraintValidator<PositiveOrZero, String> {

    @Override
    public boolean isValid(String number, ConstraintValidatorContext constraintValidatorContext) {
        if (number == null || number.isEmpty()) return true;
        try {
            return java.lang.Long.parseLong(number) >= 0;
        } catch (Exception e) {
            return false;
        }
    }
}
