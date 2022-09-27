package com.tuannq.authentication.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongValidator implements ConstraintValidator<Long, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) return true;
        try {
            java.lang.Long.parseLong(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
