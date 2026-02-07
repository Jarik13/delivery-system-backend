package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Schema(description = "Об'єкт передачі даних для графіку роботи відділення")
public record WorkScheduleDto(
        @Schema(description = "Унікальний ідентифікатор запису", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Ідентифікатор дня тижня (зазвичай 1 - Понеділок, 7 - Неділя)", example = "1")
        Integer dayOfWeekId,

        @Schema(description = "Назва дня тижня (заповнюється автоматично)",
                example = "Понеділок",
                accessMode = Schema.AccessMode.READ_ONLY)
        String dayOfWeekName,

        @NotNull
        @Schema(description = "Ідентифікатор часового інтервалу з довідника", example = "5")
        Integer workTimeIntervalId,

        @Schema(description = "Час початку роботи (заповнюється автоматично)",
                example = "08:00:00",
                accessMode = Schema.AccessMode.READ_ONLY)
        LocalTime startTime,

        @Schema(description = "Час закінчення роботи (заповнюється автоматично)",
                example = "20:00:00",
                accessMode = Schema.AccessMode.READ_ONLY)
        LocalTime endTime,

        @NotNull
        @Schema(description = "Ідентифікатор відділення, до якого прив'язаний цей графік", example = "2")
        Integer branchId
) {}