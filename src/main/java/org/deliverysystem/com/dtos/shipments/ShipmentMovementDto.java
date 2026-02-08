package org.deliverysystem.com.dtos.shipments;

import java.time.LocalDateTime;

public record ShipmentMovementDto(
        LocalDateTime time,
        String locationName,
        String statusDescription
) {}
