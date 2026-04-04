package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Елемент маршрутного листа (конкретне завдання кур'єра)")
public record RouteSheetItemDto(
        @Schema(description = "ID запису в листі")
        Integer id,

        @Schema(description = "Трек-номер відправлення", example = "59000000000379")
        String trackingNumber,

        @Schema(description = "ПІБ отримувача", example = "Шиян Левко Болеславович")
        String recipientFullName,

        @Schema(description = "Телефон отримувача", example = "+380501234567")
        String recipientPhone,

        @Schema(description = "Повна адреса доставки", example = "м. Київ, вул. Хрещатик, 1, кв. 15")
        String deliveryAddress,

        @Schema(description = "Адреса призначення (куди доставити)", example = "м. Київ, вул. Хрещатик, 1, кв. 15")
        String destinationAddress,

        @Schema(description = "Вага (кг)", example = "2.5")
        BigDecimal weight,

        @Schema(description = "Сума післяплати (грн)", example = "500.00")
        BigDecimal codAmount,

        @Schema(description = "Статус (вручено/не вручено)")
        boolean isDelivered,

        @Schema(description = "Час фактичної доставки")
        LocalDateTime deliveredAt,

        @Schema(description = "ID відправлення")
        Integer shipmentId,

        @Schema(description = "Статус відправлення")
        String shipmentStatusName,

        @Schema(description = "Чи є борг по оплаті")
        Boolean hasCod,

        @Schema(description = "Сума залишку до оплати")
        BigDecimal remainingAmount,

        @Schema(description = "Загальна вартість відправлення")
        BigDecimal totalPrice
) {}