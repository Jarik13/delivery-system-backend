package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ParcelSearchCriteria(
        @Schema(description = "Пошук в описі вмісту")
        String name,

        @Schema(description = "ID типу посилки")
        Integer parcelTypeId,

        @Schema(description = "Мін. вага")
        BigDecimal weightMin,

        @Schema(description = "Макс. вага")
        BigDecimal weightMax,

        @Schema(description = "Мін. оголошена вартість")
        BigDecimal declaredValueMin,

        @Schema(description = "Макс. оголошена вартість")
        BigDecimal declaredValueMax
) {}