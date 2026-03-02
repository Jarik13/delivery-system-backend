package org.deliverysystem.com.dtos.waybills;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateWaybillDto(
        @NotNull(message = "Оберіть рейс для накладної")
        Integer tripId,

        @NotNull(message = "Оберіть сегмент маршруту")
        Integer routeId,

        @NotNull(message = "Порядковий номер сегменту є обов'язковим")
        @Positive(message = "Порядковий номер сегменту повинен бути більшим за нуль")
        Integer tripSequenceNumber,

        @NotEmpty(message = "Оберіть хоча б одне відправлення для накладної")
        List<Integer> shipmentIds
) {}