package org.deliverysystem.com.dtos.payments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Платіж")
public record PaymentDto(
        @Schema(description = "Унікальний ідентифікатор платежу", example = "55")
        Integer id,

        @NotBlank
        @Schema(description = "Номер транзакції", example = "TXN-999888")
        String transactionNumber,

        @NotNull
        @Schema(description = "Сума", example = "120.50")
        BigDecimal amount,

        @Schema(description = "Дата оплати")
        LocalDateTime paymentDate,

        @NotNull
        @Schema(description = "ID типу оплати (карта/готівка)", example = "1")
        Integer paymentTypeId,

        @Schema(description = "Назва типу оплати (Готівка, Карта тощо)", accessMode = Schema.AccessMode.READ_ONLY)
        String paymentTypeName,

        @NotNull
        @Schema(description = "ID відправлення", example = "100")
        Integer shipmentId
) {}