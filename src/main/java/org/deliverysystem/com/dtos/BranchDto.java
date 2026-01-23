package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Відділення")
public record BranchDto(
        @Schema(description = "ID відділення", example = "1")
        Integer id,

        @NotBlank
        @Schema(description = "Назва", example = "Відділення №1")
        String name,

        @NotBlank
        @Schema(description = "Адреса", example = "вул. Шевченка, 10")
        String address,

        @NotNull
        @Schema(description = "ID міста (для форми)", example = "5")
        Integer cityId,

        @Schema(description = "Назва міста (для відображення)", example = "Київ", accessMode = Schema.AccessMode.READ_ONLY)
        String cityName,

        @NotNull
        @Schema(description = "ID типу відділення (для форми)", example = "2")
        Integer branchTypeId,

        @Schema(description = "Назва типу (для відображення)", example = "Вантажне", accessMode = Schema.AccessMode.READ_ONLY)
        String branchTypeName
) {}