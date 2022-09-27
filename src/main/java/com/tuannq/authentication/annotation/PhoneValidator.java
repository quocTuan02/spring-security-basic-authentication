package com.tuannq.authentication.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;

import static com.tuannq.authentication.model.type.RegexType.PHONE_PATTERN;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) return true;

        Matcher matcher = PHONE_PATTERN.matcher(s);
        return matcher.matches();
    }
}
