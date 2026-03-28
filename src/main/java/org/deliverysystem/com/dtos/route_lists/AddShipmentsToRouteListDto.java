package org.deliverysystem.com.dtos.route_lists;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "Додавання відправлень до існуючого маршрутного листа")
public record AddShipmentsToRouteListDto(
        @NotEmpty(message = "Список відправлень не може бути порожнім")
        @Schema(description = "IDs відправлень для додавання")
        List<Integer> shipmentIds
) {}