package org.deliverysystem.com.dtos.waybills;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateWaybillDto(
        @NotNull(message = "Рейс обов'язковий")
        Integer tripId,

        @NotNull(message = "Маршрут обов'язковий")
        Integer routeId,

        @NotNull(message = "Порядковий номер сегменту обов'язковий")
        @Positive
        Integer tripSequenceNumber,

        @NotEmpty(message = "Накладна має містити хоча б одне відправлення")
        List<Integer> shipmentIds
) {}