package org.deliverysystem.com.dtos.routes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Шаблон магістрального маршруту")
public record RouteDto(
        @Schema(description = "Унікальний ідентифікатор маршруту", example = "25")
        Integer id,

        @Schema(description = "ID відділення відправлення", example = "1")
        Integer originBranchId,

        @Schema(description = "Назва відділення відправлення", example = "Відділення №1", accessMode = Schema.AccessMode.READ_ONLY)
        String originBranchName,

        @Schema(description = "Назва міста відправлення", example = "Київ", accessMode = Schema.AccessMode.READ_ONLY)
        String originCityName,

        @Schema(description = "ID відділення призначення", example = "2")
        Integer destinationBranchId,

        @Schema(description = "Назва відділення призначення", example = "Відділення №5", accessMode = Schema.AccessMode.READ_ONLY)
        String destinationBranchName,

        @Schema(description = "Назва міста призначення", example = "Львів", accessMode = Schema.AccessMode.READ_ONLY)
        String destinationCityName,

        @Schema(description = "ID міста відправлення", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
        Integer originCityId,

        @Schema(description = "ID міста призначення", example = "34", accessMode = Schema.AccessMode.READ_ONLY)
        Integer destinationCityId,

        @Schema(description = "Чи потрібне сортування на маршруті", example = "false")
        Boolean needSorting,

        @Schema(description = "Відстань маршруту в кілометрах", example = "450.5")
        Float distanceKm
) {}