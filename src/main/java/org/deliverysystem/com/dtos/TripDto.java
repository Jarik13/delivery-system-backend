package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Магістральний рейс")
public record TripDto(
        @Schema(description = "Унікальний ідентифікатор рейсу", example = "55")
        Integer id,

        @NotNull
        @Schema(description = "Номер рейсу", example = "7755")
        Integer tripNumber,

        @Schema(description = "Плановий час виїзду")
        LocalDateTime scheduledDepartureTime,

        @Schema(description = "Плановий час прибуття")
        LocalDateTime scheduledArrivalTime,

        @Schema(description = "Фактичний час виїзду", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime actualDepartureTime,

        @Schema(description = "Фактичний час прибуття", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime actualArrivalTime,

        @NotNull
        @Schema(description = "ID статусу рейсу", example = "1")
        Integer tripStatusId,

        @NotNull
        @Schema(description = "ID водія", example = "10")
        Integer driverId,

        @NotNull
        @Schema(description = "ID транспортного засобу", example = "5")
        Integer vehicleId,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String status,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String originCity,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String destinationCity,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String driverName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String vehiclePlate,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer shipmentsCount,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) BigDecimal totalWeight,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer distanceKm,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) List<String> waypoints
) {}