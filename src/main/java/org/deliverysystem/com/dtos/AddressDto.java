package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Повна адреса клієнта (з квартирою)")
public record AddressDto(
        @Schema(description = "ID адреси", example = "1234")
        Integer id,

        @NotNull
        @Schema(description = "ID будинку", example = "100")
        Integer houseId,

        @Schema(description = "Номер квартири/офісу", example = "45")
        Integer apartmentNumber
) {}