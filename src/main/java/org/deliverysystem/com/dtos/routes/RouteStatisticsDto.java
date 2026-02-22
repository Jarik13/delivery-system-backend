package org.deliverysystem.com.dtos.routes;

import io.swagger.v3.oas.annotations.media.Schema;

public record RouteStatisticsDto(
        @Schema(description = "Мінімальна відстань між відділенями", example = "0")
        Float distanceKmMin,

        @Schema(description = "Максимальна відстань між відділенями вага", example = "3000")
        Float distanceKmMax
) {}
