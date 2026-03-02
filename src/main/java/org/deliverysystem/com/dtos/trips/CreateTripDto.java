package org.deliverysystem.com.dtos.trips;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Дані для створення нового рейсу")
public record CreateTripDto(
        @NotNull(message = "Оберіть водія для рейсу")
        @Schema(description = "ID водія", example = "5")
        Integer driverId,

        @NotNull(message = "Оберіть транспортний засіб для рейсу")
        @Schema(description = "ID транспортного засобу", example = "12")
        Integer vehicleId,

        @NotNull(message = "Вкажіть плановий час відправлення")
        @Schema(description = "Плановий час відправлення")
        LocalDateTime scheduledDepartureTime,

        @NotNull(message = "Вкажіть плановий час прибуття")
        @Schema(description = "Плановий час прибуття")
        LocalDateTime scheduledArrivalTime,

        @NotEmpty(message = "Маршрут повинен містити хоча б два міста")
        @Size(min = 2, message = "Маршрут повинен містити хоча б два міста")
        @Valid
        @Schema(description = "Проміжні точки маршруту")
        List<WaypointInputDto> waypoints
) {}