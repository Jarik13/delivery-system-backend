package org.deliverysystem.com.dtos.returns;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ReturnStatisticsDto(
        @Schema(description = "Мінімальна сума повернення", example = "10.00")
        BigDecimal refundAmountMin,

        @Schema(description = "Мінімальна сума повернення", example = "10.00")
        BigDecimal refundAmountMax
) {}