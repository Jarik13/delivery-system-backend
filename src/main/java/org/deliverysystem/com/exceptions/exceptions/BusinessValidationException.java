package org.deliverysystem.com.exceptions.exceptions;

import lombok.Getter;
import java.util.Map;

@Getter
public class BusinessValidationException extends RuntimeException {
    private final Map<String, String> validationErrors;

    public BusinessValidationException(Map<String, String> validationErrors) {
        super("Помилка валідації");
        this.validationErrors = validationErrors;
    }

    public BusinessValidationException(String field, String message) {
        super("Помилка валідації");
        this.validationErrors = Map.of(field, message);
    }
}