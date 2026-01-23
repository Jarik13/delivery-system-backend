package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Відправлення (документ)")
public record ShipmentDto(
        @Schema(description = "Унікальний ідентифікатор відправлення", example = "100")
        Integer id,

        @NotBlank
        @Schema(description = "Трек-номер", example = "UA123456789")
        String trackingNumber,

        @Schema(description = "Загальна вартість доставки", example = "120.50")
        BigDecimal totalPrice,

        @Schema(description = "Чи оплачує відправник", example = "true")
        Boolean isSenderPay,

        @Schema(description = "Дата створення")
        LocalDateTime createdAt,

        @NotNull @Schema(description = "ID відправника") Integer senderId,
        @NotNull @Schema(description = "ID отримувача") Integer recipientId,
        @NotNull @Schema(description = "ID посилки") Integer parcelId,
        @NotNull @Schema(description = "ID типу відправлення") Integer shipmentTypeId,
        @NotNull @Schema(description = "ID статусу") Integer shipmentStatusId,
        @Schema(description = "ID співробітника (автора)") Integer createdById
) {}