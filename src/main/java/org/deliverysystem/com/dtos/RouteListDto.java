package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

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
        @Schema(description = "ID кур'єра", example = "10")
        Integer courierId,

        @NotNull
        @Schema(description = "ID статусу виконання", example = "1")
        Integer statusId
) {}