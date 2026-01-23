package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Дані району")
public record DistrictDto(
        @Schema(description = "Унікальний ID", example = "10")
        Integer id,

        @NotBlank
        @Schema(description = "Назва району", example = "Білоцерківський")
        String name,

        @NotNull
        @Schema(description = "ID області", example = "1")
        Integer regionId
) {}