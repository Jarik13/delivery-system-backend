package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;

@Schema(description = "Фізичні параметри посилки")
public record ParcelDto(
        @Schema(description = "Унікальний ідентифікатор посилки", example = "500")
        Integer id,

        @NotNull
        @PositiveOrZero
        @Schema(description = "Оголошена вартість (грн)", example = "1500.00")
        BigDecimal declaredValue,

        @NotNull
        @Positive
        @Schema(description = "Фактична вага (кг)", example = "2.5")
        BigDecimal actualWeight,

        @Schema(description = "Опис вмісту", example = "Книги та документи")
        String contentDescription,

        @NotNull
        @Schema(description = "ID типу посилки", example = "1")
        Integer parcelTypeId,

        @Schema(description = "Назва типу посилки", example = "Електроніка", accessMode = Schema.AccessMode.READ_ONLY)
        String parcelTypeName,

        @Schema(description = "ID умов зберігання", example = "[1, 3]")
        Set<Integer> storageConditionIds,

        @Schema(description = "Назви умов зберігання (Тільки для читання)", example = "[\"Крихке\", \"Не перевертати\"]", accessMode = Schema.AccessMode.READ_ONLY)
        Set<String> storageConditionNames
) {}