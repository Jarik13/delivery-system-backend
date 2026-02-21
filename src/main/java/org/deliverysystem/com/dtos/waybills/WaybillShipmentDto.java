package org.deliverysystem.com.dtos.waybills;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Відправлення у складі накладної")
public record WaybillShipmentDto(
        @Schema(description = "ID відправлення")
        Integer id,

        @Schema(description = "Трек-номер", example = "59000000000379")
        String trackingNumber,

        @Schema(description = "Порядковий номер у накладній")
        Integer sequenceNumber,

        @Schema(description = "ПІБ відправника", example = "Сосюра Зеновій Орестович")
        String senderFullName,

        @Schema(description = "ПІБ отримувача", example = "Шиян Левко Болеславович")
        String recipientFullName,

        @Schema(description = "Місто відправлення", example = "місто Одеса")
        String originCityName,

        @Schema(description = "Місто призначення", example = "місто Київ")
        String destinationCityName,

        @Schema(description = "Фактична вага (кг)", example = "2.6")
        BigDecimal actualWeight,

        @Schema(description = "Загальна вартість", example = "118.70")
        BigDecimal totalPrice,

        @Schema(description = "Назва статусу", example = "Доставлено")
        String shipmentStatusName,

        @Schema(description = "Дата створення")
        LocalDateTime createdAt
) {}