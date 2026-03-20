package org.deliverysystem.com.dtos.shipments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RouteLocationDto(
        @NotBlank(message = "Тип локації має бути вказаний (BRANCH, POSTOMAT, ADDRESS)")
        String type,

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