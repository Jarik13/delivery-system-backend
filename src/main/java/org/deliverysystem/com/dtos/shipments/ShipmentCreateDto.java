package org.deliverysystem.com.dtos.shipments;

import jakarta.validation.constraints.NotNull;
import org.deliverysystem.com.dtos.parcels.ParcelDto;

import java.math.BigDecimal;

public record ShipmentCreateDto(
        @NotNull ParcelDto parcel,
        BigDecimal deliveryPrice,
        BigDecimal weightPrice,
        BigDecimal distancePrice,
        BigDecimal boxVariantPrice,
        BigDecimal specialPackagingPrice,
        BigDecimal insuranceFee,
        @NotNull BigDecimal totalPrice,
        @NotNull Boolean isSenderPay,
        @NotNull Boolean isPartiallyPaid,
        @NotNull Integer senderId,
        @NotNull Integer recipientId,
        @NotNull Integer shipmentTypeId,
        @NotNull Integer shipmentStatusId
) {}
