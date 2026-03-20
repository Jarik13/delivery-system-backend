package org.deliverysystem.com.dtos.shipments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Дані для оформлення нового відправлення")
public record CreateShipmentDto(
        @NotNull(message = "Оголошена вартість має бути вказана")
        @Positive(message = "Оголошена вартість має бути більшою за 0")
        @Schema(description = "Сума страхування вантажу")
        BigDecimal declaredValue,

        @NotNull(message = "Фактична вага має бути вказана")
        @DecimalMin(value = "0.01", message = "Мінімальна вага для оформлення — 0.01 кг")
        @Schema(description = "Вага вантажу в кг")
        BigDecimal actualWeight,

        @NotBlank(message = "Опис вмісту не може бути порожнім")
        @Size(min = 2, max = 50, message = "Опис вмісту має бути від 2 до 50 символів")
        @Schema(description = "Опис того, що всередині", example = "Одяг та взуття")
        String contentDescription,

        @NotNull(message = "Необхідно вказати категорію посилки")
        @Schema(description = "ID типу посилки")
        Integer parcelTypeId,

        @Schema(description = "Список ID обраних умов зберігання")
        List<Integer> storageConditionIds,

        @NotNull(message = "Необхідно обрати відправника")
        @Schema(description = "ID клієнта-відправника")
        Integer senderId,

        @NotNull(message = "Необхідно обрати отримувача")
        @Schema(description = "ID клієнта-отримувача")
        Integer recipientId,

        @NotNull(message = "Оберіть тип доставки")
        @Schema(description = "ID типу відправлення (Експрес/Стандарт)")
        Integer shipmentTypeId,

        @NotNull(message = "Дані про точку відправлення обов'язкові")
        @Valid
        @Schema(description = "Локація відправлення")
        RouteLocationDto origin,

        @NotNull(message = "Дані про точку отримання обов'язкові")
        @Valid
        @Schema(description = "Локація призначення")
        RouteLocationDto destination,

        @NotNull(message = "Вкажіть, хто сплачує за послуги")
        @Schema(description = "true - відправник, false - отримувач")
        Boolean isSenderPay,

        @NotNull(message = "Вкажіть статус часткової оплати")
        @Schema(description = "Чи була проведена передплата")
        Boolean isPartiallyPaid,

        @DecimalMin(value = "0.0", message = "Сума авансу не може бути від'ємною")
        @Schema(description = "Сума попередньої оплати (якщо є)")
        BigDecimal partialAmount,

        @Schema(description = "ID варіанту пакування")
        Integer boxVariantId,

        @Schema(description = "ID способу оплати")
        Integer paymentTypeId,

        @Schema(description = "Чи повністю оплачено")
        Boolean isFullyPaid,

        @NotNull(message = "Дані розрахунку вартості мають бути додані до запиту")
        @Valid
        @Schema(description = "Результати тарифікації для перевірки")
        CalculatedPriceResponseDto calculatedPrice
) {}