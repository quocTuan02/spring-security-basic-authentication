package com.tuannq.authentication.annotation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.tuannq.authentication.model.type.RegexType.SLUG_PATTERN;

public class SlugValidator implements ConstraintValidator<Slug, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) return true;

        var matcher = SLUG_PATTERN.matcher(s);

        return matcher.matches();
    }
}
