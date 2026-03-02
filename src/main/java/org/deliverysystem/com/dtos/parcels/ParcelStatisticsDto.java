package org.deliverysystem.com.dtos.parcels;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Статистика по посилках")
public record ParcelStatisticsDto(
        @Schema(description = "Мінімальна вага", example = "0.1")
        BigDecimal minWeight,

        @Schema(description = "Максимальна вага", example = "150.0")
        BigDecimal maxWeight,

        @Schema(description = "Мінімальна вартість", example = "10.00")
        BigDecimal minDeclaredValue,

        @Schema(description = "Максимальна вартість", example = "100000.00")
        BigDecimal maxDeclaredValue
) {}