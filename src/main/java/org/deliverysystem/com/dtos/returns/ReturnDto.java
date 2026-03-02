package org.deliverysystem.com.dtos.returns;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Повернення вантажу")
public record ReturnDto(
        @Schema(description = "Унікальний ідентифікатор повернення", example = "10")
        Integer id,

        @NotBlank
        @Schema(description = "Трек-номер повернення", example = "RET-123456")
        String returnTrackingNumber,

        @Schema(description = "Дата ініціації")
        LocalDateTime initiationDate,

        @Schema(description = "Дата завершення", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime completionDate,

        @Schema(description = "Сума до повернення", example = "500.00")
        BigDecimal refundAmount,

        @NotNull
        @Schema(description = "ID оригінального відправлення", example = "100")
        Integer shipmentId,

        @NotNull
        @Schema(description = "ID причини повернення", example = "2")
        Integer returnReasonId,

        @Schema(description = "Назва причини повернення (текст)", accessMode = Schema.AccessMode.READ_ONLY)
        String returnReasonName
) {}