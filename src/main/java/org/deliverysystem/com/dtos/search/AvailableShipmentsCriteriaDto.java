package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Критерії пошуку відправлень, що підходять під сегмент рейсу")
public record AvailableShipmentsCriteriaDto(
        @Schema(description = "ID маршруту (Route)", example = "33091", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ID маршруту є обов'язковим")
        Integer routeId,

        @Schema(description = "Пошук за трек-номером", example = "59000000105938")
        String trackingNumber
) {}