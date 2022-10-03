package com.tuannq.authentication.annotation;


import com.tuannq.authentication.model.type.UserType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<Role, String> {
    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        if (role == null || role.isEmpty()) return true;
        return Arrays.stream(UserType.values()).map(UserType::getRole).anyMatch(r -> r.equals(role));
    }
}
