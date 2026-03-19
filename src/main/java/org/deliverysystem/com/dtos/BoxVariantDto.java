package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Варіант пакувальної коробки")
public record BoxVariantDto(
        Integer id,

        @NotNull(message = "Назва не може бути порожньою")
        String name,

        @NotNull(message = "Ціна не може бути порожньою")
        @Positive(message = "Ціна має бути більшою за 0")
        BigDecimal price,

        @NotNull(message = "Ширина обов'язкова")
        @Positive(message = "Ширина має бути додатною")
        BigDecimal width,

        @NotNull(message = "Довжина обов'язкова")
        @Positive(message = "Довжина має бути додатною")
        BigDecimal length,

        @NotNull(message = "Висота обов'язкова")
        @Positive(message = "Висота має бути додатною")
        BigDecimal height,

        @Schema(description = "Максимальна вага вмісту (кг)", example = "10.0")
        BigDecimal maxWeight,

        @Schema(description = "Назва категорії ваги", example = "до 10 кг")
        String weightCategoryName,

        @NotNull(message = "ID типу коробки обов'язковий")
        Integer boxTypeId,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        String boxTypeName
) {}