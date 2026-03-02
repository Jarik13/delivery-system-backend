package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Варіант пакувальної коробки")
public record BoxVariantDto(
        @Schema(description = "ID варіанту коробки", example = "1")
        Integer id,

        @NotNull(message = "Назва не може бути порожньою")
        @Schema(description = "Назва варіанту пакування", example = "S")
        String name,

        @NotNull(message = "Ціна не може бути порожньою")
        @Positive(message = "Ціна має бути більшою за 0")
        @Schema(description = "Ціна коробки", example = "25.50")
        BigDecimal price,

        @NotNull(message = "Ширина обов'язкова")
        @Positive(message = "Ширина має бути додатною")
        @Schema(description = "Ширина (см)", example = "30.0")
        BigDecimal width,

        @NotNull(message = "Довжина обов'язкова")
        @Positive(message = "Довжина має бути додатною")
        @Schema(description = "Довжина (см)", example = "40.0")
        BigDecimal length,

        @NotNull(message = "Висота обов'язкова")
        @Positive(message = "Висота має бути додатною")
        @Schema(description = "Висота (см)", example = "20.0")
        BigDecimal height,

        @NotNull(message = "ID типу коробки обов'язковий")
        @Schema(description = "ID типу коробки", example = "2")
        Integer boxTypeId,

        @Schema(description = "Назва типу коробки (тільки для читання)", accessMode = Schema.AccessMode.READ_ONLY)
        String boxTypeName
) {}