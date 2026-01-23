package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PaymentTypeDto(
        @Schema(description = "ID типу оплати", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Спосіб оплати", example = "Готівка")
        String name
) {}
