package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Фізичні параметри посилки")
public record ParcelDto(
        @Schema(description = "Унікальний ідентифікатор посилки", example = "500")
        Integer id,

        @NotNull
        @Schema(description = "Оголошена вартість", example = "1500.00")
        BigDecimal declaredValue,

        @NotNull
        @Schema(description = "Фактична вага (кг)", example = "2.5")
        BigDecimal actualWeight,

        @Schema(description = "Опис вмісту", example = "Книги та документи")
        String contentDescription,

        @NotNull
        @Schema(description = "ID типу посилки", example = "1")
        Integer parcelTypeId
) {}