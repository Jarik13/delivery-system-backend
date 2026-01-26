package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostomatSearchCriteria(
        @Schema(description = "Назва поштомату", example = "Поштомат №1")
        String name,

        @Schema(description = "Технічний код", example = "3005")
        String code,

        @Schema(description = "Адреса", example = "вул. Шевченка, 10")
        String address,

        @Schema(description = "ID міста")
        Integer cityId,

        @Schema(description = "Статус активності", example = "true")
        Boolean isActive,

        @Schema(description = "Мінімальна кількість комірок")
        Integer cellsCountMin,

        @Schema(description = "Максимальна кількість комірок")
        Integer cellsCountMax
) {}