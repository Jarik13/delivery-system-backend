package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

public record RouteListStatisticsDto(
        @Schema(description = "Мінімальна вага у маршрутному листі", example = "5.00")
        BigDecimal totalWeightMin,

        @Schema(description = "Максимальна вага у маршрутному листі", example = "30.00")
        BigDecimal totalWeightMax,

        @Schema(description = "Кількість маршрутних листів по кожному статусу (statusId -> count)")
        Map<Integer, Long> countByStatus
) {}