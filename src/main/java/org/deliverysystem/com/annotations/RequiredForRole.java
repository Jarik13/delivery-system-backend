package org.deliverysystem.com.annotations;

import org.deliverysystem.com.enums.Role;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredForRole {
    Role[] roles();
    String field();
    String message() default "";
}