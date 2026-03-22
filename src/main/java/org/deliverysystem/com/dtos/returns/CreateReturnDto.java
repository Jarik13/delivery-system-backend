package org.deliverysystem.com.dtos.returns;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Дані для створення повернення")
public record CreateReturnDto(
        @NotNull
        @Schema(description = "ID відправлення", example = "100")
        Integer shipmentId,

        @NotNull
        @Schema(description = "ID причини повернення", example = "2")
        Integer returnReasonId,

        @Positive
        @Schema(description = "Сума до повернення", example = "500.00")
        BigDecimal refundAmount
) {}