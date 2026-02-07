package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Відправлення (документ)")
public record ShipmentDto(
        Integer id,
        String trackingNumber,
        LocalDateTime createdAt,

        BigDecimal deliveryPrice,
        BigDecimal weightPrice,
        BigDecimal distancePrice,
        BigDecimal boxVariantPrice,
        BigDecimal specialPackagingPrice,
        BigDecimal insuranceFee,
        BigDecimal totalPrice,

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
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String createdByFullName
) {}