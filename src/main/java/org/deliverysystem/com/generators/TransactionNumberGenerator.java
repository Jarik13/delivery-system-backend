package org.deliverysystem.com.generators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.UUID;

public class TransactionNumberGenerator implements BeforeExecutionGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        LocalDateTime date = LocalDateTime.now();
        long timestamp = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String randomPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "TRX-" + timestamp + "-" + randomPart;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
