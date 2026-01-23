package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Поштомат")
public record PostomatDto(
        @Schema(description = "ID поштомату", example = "1")
        Integer id,

        @NotBlank
        @Schema(description = "Назва", example = "Поштомат №2045")
        String name,

        @NotBlank
        @Schema(description = "Адреса", example = "в магазині АТБ")
        String address,

        @NotNull
        @Schema(description = "ID міста (для форми)", example = "5")
        Integer cityId,

        @Schema(description = "Назва міста (для відображення)", example = "Львів", accessMode = Schema.AccessMode.READ_ONLY)
        String cityName,

        @NotBlank
        @Schema(description = "Технічний код", example = "P-2045")
        String code,

        @Schema(description = "Кількість комірок")
        Integer cellsCount,

        @Schema(description = "Чи активний")
        Boolean isActive
) {}