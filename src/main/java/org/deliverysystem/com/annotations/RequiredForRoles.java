package org.deliverysystem.com.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.deliverysystem.com.validators.RequiredForRoleValidator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RequiredForRoleValidator.class)
public @interface RequiredForRoles {
    RequiredForRole[] value();
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}