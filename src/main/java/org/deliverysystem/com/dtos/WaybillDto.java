package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Транспортна накладна (Маніфест)")
public record WaybillDto(
        @Schema(description = "Унікальний ідентифікатор", example = "99")
        Integer id,

        @NotNull
        @Schema(description = "Номер документу", example = "889900")
        Integer number,

        @Schema(description = "Загальна вага", example = "500.00")
        BigDecimal totalWeight,

        @Schema(description = "Загальний об'єм", example = "10.5")
        BigDecimal volume,

        @NotNull
        @Schema(description = "ID співробітника, що створив", example = "2")
        Integer createdById
) {}