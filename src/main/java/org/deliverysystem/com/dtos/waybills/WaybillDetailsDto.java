package org.deliverysystem.com.dtos.waybills;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record WaybillDetailsDto(
        Integer waybillId,
        Integer waybillNumber,
        LocalDateTime createdAt,
        BigDecimal totalWeight,
        BigDecimal totalVolume,
        String employeeName,
        List<WaybillShipmentDto> shipments
) {}