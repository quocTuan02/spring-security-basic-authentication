package com.tuannq.authentication.annotation;


import com.tuannq.authentication.model.type.UserType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class RoleValidator implements ConstraintValidator<Role, List<String>> {
    @Override
    public boolean isValid(List<String> roles, ConstraintValidatorContext constraintValidatorContext) {
        if (roles == null || roles.isEmpty()) return true;
        for (var role : roles) {
            if (Arrays.stream(UserType.values()).map(UserType::getRole).noneMatch(r -> r.equals(role)))
                return false;
        }
        return true;
    }
}
