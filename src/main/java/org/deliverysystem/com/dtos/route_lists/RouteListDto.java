package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Маршрутний лист кур'єра (завдання на день)")
public record RouteListDto(
        @Schema(description = "Унікальний ідентифікатор листа", example = "500")
        Integer id,

        @NotNull
        @Schema(description = "Номер маршрутного листа", example = "102030")
        Integer number,

        @Schema(description = "Загальна вага вантажу (кг)", example = "150.50")
        BigDecimal totalWeight,

        @NotNull
        @Schema(description = "ID кур'єра")
        Integer courierId,

        @Schema(description = "ПІБ кур'єра", example = "Петренко Іван Іванович")
        String courierFullName,

        @NotNull
        @Schema(description = "ID статусу виконання")
        Integer statusId,

        @Schema(description = "Назва статусу", example = "У процесі доставки")
        String statusName,

        @Schema(description = "Дата та час створення")
        LocalDateTime createdAt,

        @Schema(description = "Список відправлень у маршруті")
        List<RouteSheetItemDto> items
) {}