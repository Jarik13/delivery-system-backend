package org.deliverysystem.com.dtos.shipments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.deliverysystem.com.enums.DeliveryLocationType;

public record RouteLocationDto(
        @NotNull
        DeliveryLocationType type,

        @Schema(description = "ID відділення або поштомату")
        Integer deliveryPointId,

        @Schema(description = "ID міста")
        Integer cityId,

        @Schema(description = "ID вулиці (для адресної доставки)")
        Integer streetId,

        @Size(max = 10, message = "Номер будинку занадто довгий")
        String houseNumber,

        @Schema(description = "Номер квартири")
        Integer apartmentNumber
) {}