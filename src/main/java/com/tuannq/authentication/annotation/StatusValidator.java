package com.tuannq.authentication.annotation;


import com.tuannq.authentication.model.type.StatusType;
import com.tuannq.authentication.model.type.UserType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class StatusValidator implements ConstraintValidator<Status, String> {
    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        if (role == null || role.isEmpty()) return true;
        return Arrays.stream(StatusType.values())
                .map(Enum::name)
                .anyMatch(r -> r.equals(role));
    }
}
