package org.deliverysystem.com.generators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;
import java.util.UUID;

public class TransactionNumberGenerator implements BeforeExecutionGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        long timestamp = System.currentTimeMillis();
        String uuidPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "TRX-" + timestamp + "-" + uuidPart;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
