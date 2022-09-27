package com.tuannq.authentication.annotation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.tuannq.authentication.model.type.RegexType.EMAIL_PATTERN;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || email.isEmpty()) return true;
        var matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
