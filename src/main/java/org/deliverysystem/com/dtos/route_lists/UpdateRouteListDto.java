package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Дані для оновлення маршрутного листа")
public record UpdateRouteListDto(
        @NotNull(message = "Оберіть кур'єра для маршруту")
        @Schema(description = "ID кур'єра", example = "3")
        Integer courierId,

        @NotNull(message = "Вкажіть плановий час виїзду")
        @Future(message = "Плановий час виїзду не може бути у минулому")
        @Schema(description = "Плановий час виїзду кур'єра")
        LocalDateTime plannedDepartureTime
) {}