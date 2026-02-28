package org.deliverysystem.com.dtos.routes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO для створення/оновлення магістрального маршруту")
public record CreateRouteDto(
        @NotNull(message = "Оберіть відділення відправлення")
        @Schema(description = "ID відділення відправлення", example = "1")
        Integer originBranchId,

        @NotNull(message = "Оберіть відділення призначення")
        @Schema(description = "ID відділення призначення", example = "2")
        Integer destinationBranchId,

        @Schema(description = "Чи потрібне сортування на маршруті", example = "false")
        Boolean needSorting,

        @NotNull(message = "Вкажіть відстань маршруту")
        @Positive(message = "Відстань повинна бути більшою за нуль")
        @Schema(description = "Відстань маршруту в кілометрах", example = "450.5")
        Float distanceKm
) {}