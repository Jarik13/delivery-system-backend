package org.deliverysystem.com.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.deliverysystem.com.validators.DifferentBranchesValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentBranchesValidator.class)
public @interface DifferentBranches {
    String message() default "Відділення відправлення та призначення не можуть збігатися";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
