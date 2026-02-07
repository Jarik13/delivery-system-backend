package org.deliverysystem.com.dtos.parcels;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Schema(description = "Фізичні параметри посилки")
public record ParcelDto(
        @Schema(description = "Унікальний ідентифікатор посилки", example = "500")
        Integer id,

        @NotNull(message = "Вкажіть оголошену вартість")
        @PositiveOrZero(message = "Вартість не може бути від’ємною")
        @Schema(description = "Оголошена вартість (грн)", example = "1500.00")
        BigDecimal declaredValue,

        @NotNull(message = "Вкажіть фактичну вагу")
        @Positive(message = "Вага повинна бути більшою за нуль")
        @Schema(description = "Фактична вага (кг)", example = "2.5")
        BigDecimal actualWeight,

        @NotBlank(message = "Опис вмісту не може бути порожнім")
        @Size(max = 255, message = "Опис не може бути довшим за 255 символів")
        @Schema(description = "Опис вмісту", example = "Книги та документи")
        String contentDescription,

        @NotNull(message = "Оберіть тип посилки")
        @Schema(description = "ID типу посилки", example = "1")
        Integer parcelTypeId,

        @Schema(description = "Назва типу посилки", example = "Електроніка", accessMode = Schema.AccessMode.READ_ONLY)
        String parcelTypeName,

        @NotEmpty(message = "Оберіть хоча б одну умову зберігання")
        @Schema(description = "ID умов зберігання", example = "[1, 3]")
        Set<Integer> storageConditionIds,

        @Schema(description = "Назви умов зберігання (Тільки для читання)", example = "[\"Крихке\", \"Не перевертати\"]", accessMode = Schema.AccessMode.READ_ONLY)
        Set<String> storageConditionNames
) {}