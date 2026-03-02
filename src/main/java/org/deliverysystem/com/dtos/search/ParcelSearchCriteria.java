package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

public record ParcelSearchCriteria(
        @Schema(description = "Пошук в описі вмісту")
        String name,

        @Schema(description = "Список типів посилок")
        List<Integer> parcelTypes,

        @Schema(description = "Мін. вага")
        BigDecimal weightMin,

        @Schema(description = "Макс. вага")
        BigDecimal weightMax,

        @Schema(description = "Мін. оголошена вартість")
        BigDecimal declaredValueMin,

        @Schema(description = "Макс. оголошена вартість")
        BigDecimal declaredValueMax
) {}