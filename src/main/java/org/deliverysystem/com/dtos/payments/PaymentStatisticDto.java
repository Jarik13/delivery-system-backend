package org.deliverysystem.com.dtos.payments;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Статистика платежів для фільтрів (мін/макс значення)")
public record PaymentStatisticDto(
        @Schema(description = "Мінімальна сума платежу", example = "10.00")
        BigDecimal amountMin,

        @Schema(description = "Максимальна сума платежу", example = "49500.00")
        BigDecimal amountMax
) {}