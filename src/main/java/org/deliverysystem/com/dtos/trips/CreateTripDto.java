package org.deliverysystem.com.dtos.trips;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Дані для створення нового рейсу")
public record CreateTripDto(
        @NotNull
        @Schema(description = "ID водія", example = "5")
        Integer driverId,

        @NotNull
        @Schema(description = "ID транспортного засобу", example = "12")
        Integer vehicleId,

        @NotNull
        @Schema(description = "Плановий час відправлення")
        LocalDateTime scheduledDepartureTime,

        @NotNull
        @Schema(description = "Плановий час прибуття")
        LocalDateTime scheduledArrivalTime,

        @Schema(description = "Проміжні точки маршруту")
        List<WaypointInputDto> waypoints
) {}