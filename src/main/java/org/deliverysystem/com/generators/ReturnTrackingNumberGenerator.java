package org.deliverysystem.com.generators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

public class ReturnTrackingNumberGenerator implements BeforeExecutionGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        String suffix = String.valueOf(System.currentTimeMillis()).substring(6);
        String random = String.valueOf((int)(Math.random() * 1000));
        return "RET-" + suffix + random;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
