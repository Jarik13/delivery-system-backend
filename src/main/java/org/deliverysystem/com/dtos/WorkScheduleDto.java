package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Графік роботи")
public record WorkScheduleDto(
        @Schema(description = "ID запису", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "ID дня тижня", example = "1")
        Integer dayOfWeekId,

        @NotNull
        @Schema(description = "ID часового інтервалу", example = "5")
        Integer workTimeIntervalId,

        @NotNull
        @Schema(description = "ID відділення", example = "2")
        Integer branchId
) {}