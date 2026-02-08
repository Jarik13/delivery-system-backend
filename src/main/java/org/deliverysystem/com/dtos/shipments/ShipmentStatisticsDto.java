package org.deliverysystem.com.dtos.shipments;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Статистика меж значень відправлень для ініціалізації фільтрів (слайдерів)")
public record ShipmentStatisticsDto(
        @Schema(description = "Мінімальна фактична вага", example = "0.1")
        BigDecimal minWeight,

        @Schema(description = "Максимальна фактична вага", example = "100.0")
        BigDecimal maxWeight,


        @Schema(description = "Мінімальна загальна вартість")
        BigDecimal minTotalPrice,

        @Schema(description = "Максимальна загальна вартість")
        BigDecimal maxTotalPrice,


        @Schema(description = "Мінімальний базовий тариф")
        BigDecimal minDeliveryPrice,

        @Schema(description = "Максимальний базовий тариф")
        BigDecimal maxDeliveryPrice,


        @Schema(description = "Мінімальна доплата за вагу")
        BigDecimal minWeightPrice,

        @Schema(description = "Максимальна доплата за вагу")
        BigDecimal maxWeightPrice,


        @Schema(description = "Мінімальна доплата за відстань")
        BigDecimal minDistancePrice,

        @Schema(description = "Максимальна доплата за відстань")
        BigDecimal maxDistancePrice,


        @Schema(description = "Мінімальна ціна коробки")
        BigDecimal minBoxVariantPrice,

        @Schema(description = "Максимальна ціна коробки")
        BigDecimal maxBoxVariantPrice,


        @Schema(description = "Мінімальна ціна спец. пакування")
        BigDecimal minSpecialPackagingPrice,

        @Schema(description = "Максимальна ціна спец. пакування")
        BigDecimal maxSpecialPackagingPrice,


        @Schema(description = "Мінімальний страховий збір")
        BigDecimal minInsuranceFee,

        @Schema(description = "Максимальний страховий збір")
        BigDecimal maxInsuranceFee
) {}