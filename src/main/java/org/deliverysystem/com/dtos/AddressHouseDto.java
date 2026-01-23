package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Номер будинку")
public record AddressHouseDto(
        @Schema(description = "ID будинку", example = "100")
        Integer id,

        @NotBlank
        @Schema(description = "Номер будинку", example = "25-А")
        String number,

        @NotNull
        @Schema(description = "ID вулиці", example = "500")
        Integer streetId
) {}