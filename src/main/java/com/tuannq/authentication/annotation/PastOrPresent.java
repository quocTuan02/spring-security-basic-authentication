package com.tuannq.authentication.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastOrPresentValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PastOrPresent {
    String message() default "error.date.is-past-or-present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
