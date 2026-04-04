package org.deliverysystem.com.dtos.shipments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.deliverysystem.com.enums.DeliveryLocationType;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Запит на розрахунок вартості доставки")
public record ShipmentPriceCalculationRequestDto(
        @NotBlank(message = "Опис вмісту не може бути порожнім")
        @Size(min = 2, max = 50, message = "Опис вмісту має бути від 2 до 50 символів")
        @Schema(description = "Опис вмісту посилки")
        String contentDescription,

        @NotNull(message = "Вага є обов'язковою для розрахунку")
        @Positive(message = "Вага повинна бути більшою за 0")
        @Schema(description = "Фізична вага (кг)", example = "5.5")
        BigDecimal actualWeight,

        @NotNull(message = "Оголошена вартість є обов'язковою")
        @DecimalMin(value = "0.0", message = "Оголошена вартість не може бути від'ємною")
        @Schema(description = "Оголошена вартість для страхування", example = "1000.00")
        BigDecimal declaredValue,

        @NotNull(message = "Необхідно обрати тип посилки")
        @Schema(description = "ID типу посилки")
        Integer parcelTypeId,

        @NotEmpty(message = "Оберіть хоча б одну умову зберігання")
        @Schema(description = "Список ID додаткових умов зберігання")
        List<Integer> storageConditionIds,

        @Schema(description = "ID обраного варіанту коробки (якщо є)")
        Integer boxVariantId,

        @NotNull(message = "Необхідно обрати тип доставки (Експрес/Стандарт)")
        @Schema(description = "ID типу доставки")
        Integer shipmentTypeId,

        @NotNull(message = "Місто відправлення обов'язкове для розрахунку відстані")
        @Schema(description = "ID міста відправлення")
        Integer originCityId,

        @NotNull(message = "Місто призначення обов'язкове для розрахунку відстані")
        @Schema(description = "ID міста призначення")
        Integer destinationCityId,

        @Schema(description = "Тип локації відправлення (BRANCH / POSTOMAT / ADDRESS)")
        DeliveryLocationType originType,

        @Schema(description = "Тип локації призначення (BRANCH / POSTOMAT / ADDRESS)")
        DeliveryLocationType destinationType
) {}