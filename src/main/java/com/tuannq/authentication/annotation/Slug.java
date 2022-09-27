package com.tuannq.authentication.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SlugValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Slug {
    String message() default "slug.invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
