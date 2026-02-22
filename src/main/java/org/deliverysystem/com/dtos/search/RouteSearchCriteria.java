package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;

public record RouteSearchCriteria(
        @Schema(description = "ID відділення відправлення")
        Integer originBranchId,

        @Schema(description = "Назва відділення відправлення (пошук)", example = "Львів")
        String originBranchName,

        @Schema(description = "ID відділення призначення")
        Integer destinationBranchId,

        @Schema(description = "Назва відділення призначення (пошук)", example = "Київ")
        String destinationBranchName,

        @Schema(description = "Мінімальна відстань між відділенями")
        Float distanceKmMin,

        @Schema(description = "Максимальна відстань між відділенями")
        Float distanceKmMax,

        @Schema(description = "Чи потрібне сортування")
        Boolean needSorting
) {}