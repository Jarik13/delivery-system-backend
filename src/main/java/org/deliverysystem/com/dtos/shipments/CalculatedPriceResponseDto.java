package org.deliverysystem.com.dtos.shipments;

import java.math.BigDecimal;

public record CalculatedPriceResponseDto(
        BigDecimal deliveryPrice,
        BigDecimal weightPrice,
        BigDecimal distancePrice,
        BigDecimal boxVariantPrice,
        BigDecimal specialPackagingPrice,
        BigDecimal insuranceFee,
        BigDecimal totalPrice
) {}