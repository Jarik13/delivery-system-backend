package org.deliverysystem.com.dtos.waybills;

import java.math.BigDecimal;

public record WaybillStatisticsDto(
        BigDecimal totalWeightMin,
        BigDecimal totalWeightMax,

        BigDecimal volumeMin,
        BigDecimal volumeMax
) {}