package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Критерії пошуку міст")
public record CitySearchCriteria(
        @Schema(description = "Назва міста", example = "Львів")
        String name,

        @Schema(description = "ID району")
        Integer districtId,

        @Schema(description = "ID області")
        Integer regionId
) {}