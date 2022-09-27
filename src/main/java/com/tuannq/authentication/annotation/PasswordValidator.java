package com.tuannq.authentication.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.tuannq.authentication.model.type.RegexType.PASSWORD_PATTERN;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null || password.isEmpty()) return true;
        var matcher = PASSWORD_PATTERN.matcher(password);

        return matcher.matches() && !password.contains("\"") && !password.contains("`") && !password.contains("'");
    }
}
