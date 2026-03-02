package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Транспортний засіб")
public record VehicleDto(
        @Schema(description = "Унікальний ідентифікатор авто", example = "10")
        Integer id,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{2}\\d{4}[A-Z]{2}$", message = "Формат номеру має бути: AA1234BB")
        @Schema(description = "Державний номерний знак", example = "KA0001BC")
        String licensePlate,

        @NotNull
        @Schema(description = "ID статусу активності", example = "1")
        Integer statusId,

        @NotNull
        @Schema(description = "ID моделі авто (Fleet)", example = "5")
        Integer fleetId,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String activityStatusName,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String brandName,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String bodyTypeName,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String fuelTypeName,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String transmissionTypeName,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String driveTypeName,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        java.math.BigDecimal loadCapacity,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        java.math.BigDecimal cargoVolume,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        java.math.BigDecimal engineCapacity,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer maxSpeed
) {}