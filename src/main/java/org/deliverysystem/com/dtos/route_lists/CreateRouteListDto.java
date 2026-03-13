package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Дані для створення нового маршрутного листа")
public record CreateRouteListDto(
        @NotNull(message = "Оберіть кур'єра для маршруту")
        @Schema(description = "ID кур'єра", example = "3")
        Integer courierId,

        @NotNull(message = "Вкажіть плановий час виїзду")
        @Schema(description = "Плановий час виїзду кур'єра")
        LocalDateTime plannedDepartureTime,

        @NotEmpty(message = "Додайте хоча б одне відправлення до маршрутного листа")
        @Size(max = 13, message = "Маршрутний лист не може містити більше 13 відправлень")
        @Schema(description = "Список ID відправлень", example = "[101, 102, 103]")
        List<Integer> shipmentIds
) {}