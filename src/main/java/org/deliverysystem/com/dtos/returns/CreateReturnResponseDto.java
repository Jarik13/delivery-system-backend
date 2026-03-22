package org.deliverysystem.com.dtos.returns;

public record CreateReturnResponseDto(
        ReturnDto returnDto,
        String returnShipmentTrackingNumber
) {}