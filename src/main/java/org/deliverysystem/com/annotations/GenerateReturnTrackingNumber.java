package org.deliverysystem.com.annotations;

import org.deliverysystem.com.generators.ReturnTrackingNumberGenerator;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ValueGenerationType(generatedBy = ReturnTrackingNumberGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GenerateReturnTrackingNumber {
}
