package org.deliverysystem.com.dtos.trips;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Проміжне місто маршруту рейсу")
public record WaypointInputDto(
        @NotNull
        @Schema(description = "ID міста", example = "145")
        Integer cityId,

        @NotNull
        @Schema(description = "Порядковий номер у маршруті", example = "1")
        Integer sequenceNumber
) {}