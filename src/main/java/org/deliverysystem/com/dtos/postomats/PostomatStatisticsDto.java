package org.deliverysystem.com.dtos.postomats;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostomatStatisticsDto(
        @Schema(description = "Мінімальна кількість комірок", example = "10")
        Integer cellsCountMin,

        @Schema(description = "Максимальна кількість комірок", example = "100")
        Integer cellsCountMax
) {}