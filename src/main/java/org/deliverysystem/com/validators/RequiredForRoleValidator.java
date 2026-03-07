package org.deliverysystem.com.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.deliverysystem.com.annotations.RequiredForRole;
import org.deliverysystem.com.annotations.RequiredForRoles;
import org.deliverysystem.com.enums.Role;

import java.lang.reflect.Field;
import java.util.Arrays;

public class RequiredForRoleValidator implements ConstraintValidator<RequiredForRoles, Object> {
    private RequiredForRole[] rules;

    @Override
    public void initialize(RequiredForRoles annotation) {
        this.rules = annotation.value();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) return true;

        Role role = extractRole(obj);
        if (role == null) return true;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        for (RequiredForRole rule : rules) {
            if (Arrays.asList(rule.roles()).contains(role)) {
                Object value = extractField(obj, rule.field());
                if (value == null || (value instanceof String s && s.isBlank())) {
                    String message = rule.message().isBlank()
                            ? "Поле '" + rule.field() + "' є обов'язковим для ролі " + role.name()
                            : rule.message();

                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(rule.field())
                            .addConstraintViolation();
                    valid = false;
                }
            }
        }

        return valid;
    }

    private Role extractRole(Object obj) {
        try {
            try {
                return (Role) obj.getClass().getMethod("role").invoke(obj);
            } catch (NoSuchMethodException e) {
                Field field = obj.getClass().getDeclaredField("role");
                field.setAccessible(true);
                return (Role) field.get(obj);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Object extractField(Object obj, String fieldName) {
        try {
            try {
                return obj.getClass().getMethod(fieldName).invoke(obj);
            } catch (NoSuchMethodException e) {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            return null;
        }
    }
}