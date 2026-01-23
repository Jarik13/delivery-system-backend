package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Магістральний рейс")
public record TripDto(
        @Schema(description = "Унікальний ідентифікатор рейсу", example = "55")
        Integer id,

        @NotNull
        @Schema(description = "Номер рейсу", example = "7755")
        Integer tripNumber,

        @Schema(description = "Плановий час виїзду")
        LocalDateTime scheduledDepartureTime,

        @NotNull
        @Schema(description = "ID статусу рейсу", example = "1")
        Integer tripStatusId,

        @NotNull
        @Schema(description = "ID водія", example = "10")
        Integer driverId,

        @NotNull
        @Schema(description = "ID транспортного засобу", example = "5")
        Integer vehicleId
) {}