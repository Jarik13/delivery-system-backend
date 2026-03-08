package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Дані міста/населеного пункту")
public record CityDto(
        @Schema(description = "Унікальний ID", example = "55")
        Integer id,

        @NotBlank
        @Schema(description = "Назва міста", example = "Біла Церква")
        String name,

        @Schema(description = "Географічна довгота міста", example = "57.55")
        Double latitude,

        @Schema(description = "Географічна широта міста", example = "47.89")
        Double longitude,

        @Schema(description = "ID району (може бути null для міст обласного значення)", example = "10")
        Integer districtId
) {}