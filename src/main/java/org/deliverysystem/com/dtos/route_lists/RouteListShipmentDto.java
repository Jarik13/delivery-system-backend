package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Відправлення для вибору у маршрутний лист")
public record RouteListShipmentDto(
        @Schema(description = "ID відправлення")
        Integer id,

        @Schema(description = "Трек-номер відправлення")
        String trackingNumber,

        @Schema(description = "Повне ім'я отримувача")
        String recipientName,

        @Schema(description = "Адреса доставки або назва точки видачі")
        String deliveryAddress,

        @Schema(description = "Фактична вага посилки, кг")
        BigDecimal weight,

        @Schema(description = "Чи є відправлення експресом")
        Boolean isExpress
) {}