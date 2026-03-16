package org.deliverysystem.com.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.deliverysystem.com.validators.MinTotalWeightValidator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinTotalWeightValidator.class)
@Documented
public @interface MinTotalWeight {
    String message() default "Загальна вага відправлень повинна бути не менше 1/3 від максимальної (34 кг)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    double minFraction() default 0.33;

    double maxWeight() default 100.0;
}