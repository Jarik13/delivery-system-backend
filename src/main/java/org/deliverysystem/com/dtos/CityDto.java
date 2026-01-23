package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Дані міста/населеного пункту")
public record CityDto(
        @Schema(description = "Унікальний ID", example = "55")
        Integer id,

        @NotBlank
        @Schema(description = "Назва міста", example = "Біла Церква")
        String name,

        @Schema(description = "ID району (може бути null для міст обласного значення)", example = "10")
        Integer districtId
) {}