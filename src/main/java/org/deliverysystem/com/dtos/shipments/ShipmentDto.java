package org.deliverysystem.com.dtos.shipments;

import io.swagger.v3.oas.annotations.media.Schema;
import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.dtos.returns.ReturnDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Відправлення (документ) з фінансовими даними")
public record ShipmentDto(
        Integer id,
        String trackingNumber,
        LocalDateTime createdAt,
        LocalDateTime issuedAt,

        BigDecimal deliveryPrice,
        BigDecimal weightPrice,
        BigDecimal distancePrice,
        BigDecimal boxVariantPrice,
        BigDecimal specialPackagingPrice,
        BigDecimal insuranceFee,
        BigDecimal totalPrice,

        @Schema(description = "Скільки всього фактично оплачено")
        BigDecimal totalPaidAmount,

        @Schema(description = "Скільки залишилося оплатити")
        BigDecimal remainingAmount,

        @Schema(description = "Чи оплачено повністю")
        Boolean isFullyPaid,

        Boolean isSenderPay,
        Boolean isPartiallyPaid,

        Integer senderId,
        Integer recipientId,
        Integer parcelId,
        Integer shipmentTypeId,
        Integer shipmentStatusId,
        Integer createdById,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String senderFullName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String recipientFullName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String parcelDescription,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String shipmentTypeName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String shipmentStatusName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String createdByFullName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String originLocationName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String originCityName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String destinationLocationName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String destinationCityName,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) BigDecimal actualWeight,

        @Schema(description = "Назва варіанту коробки (тип + розміри)", accessMode = Schema.AccessMode.READ_ONLY)
        String boxVariantName,

        @Schema(description = "Розміри коробки у форматі 'ДxШxВ см'", accessMode = Schema.AccessMode.READ_ONLY)
        String boxVariantDimensions,

        @Schema(description = "Чи є спеціальне пакування", accessMode = Schema.AccessMode.READ_ONLY)
        Boolean hasSpecialPackaging,

        @Schema(description = "Тип доставки (Стандартна / Експрес / тощо)", accessMode = Schema.AccessMode.READ_ONLY)
        String deliveryTypeName,

        @Schema(description = "Список усіх платежів по цьому відправленню")
        List<PaymentDto> payments,

        @Schema(description = "Дані про всі повернення (якщо є)")
        List<ReturnDto> returns
) {}