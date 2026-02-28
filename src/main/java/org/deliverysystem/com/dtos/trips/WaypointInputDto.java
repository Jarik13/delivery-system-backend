package org.deliverysystem.com.dtos.trips;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Проміжне місто маршруту рейсу")
public record WaypointInputDto(
        @NotNull(message = "Місто маршруту не може бути порожнім")
        @Schema(description = "ID міста", example = "145")
        Integer cityId,

        @NotNull(message = "Порядковий номер є обов'язковим")
        @Min(value = 0, message = "Порядковий номер не може бути від'ємним")
        @Schema(description = "Порядковий номер у маршруті", example = "1")
        Integer sequenceNumber
) {}