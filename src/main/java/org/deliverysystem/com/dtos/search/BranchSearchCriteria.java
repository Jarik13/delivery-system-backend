package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BranchSearchCriteria(
        @Schema(description = "Пошук за назвою (часткове співпадіння)", example = "Київське")
        String name,

        @Schema(description = "Пошук за адресою (часткове співпадіння)", example = "Хрещатик")
        String address,

        @Schema(description = "ID населеного пункту")
        Integer cityId,

        @Schema(description = "Список типів відділень (ванатажне/поштове)")
        List<Integer> branchTypes
) {}