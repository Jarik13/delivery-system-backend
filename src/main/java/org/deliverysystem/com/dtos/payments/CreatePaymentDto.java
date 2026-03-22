package org.deliverysystem.com.dtos.payments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Дані для створення платежу")
public record CreatePaymentDto(
        @NotNull
        @Schema(description = "ID відправлення", example = "100")
        Integer shipmentId,

        @NotNull
        @Positive(message = "Сума має бути більше 0")
        @Schema(description = "Сума платежу", example = "120.50")
        BigDecimal amount,

        @NotNull
        @Schema(description = "ID типу оплати", example = "1")
        Integer paymentTypeId
) {}