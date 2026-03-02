package org.deliverysystem.com.dtos.search;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WaybillSearchCriteria(
        Integer number,
        BigDecimal totalWeightMin,
        BigDecimal totalWeightMax,
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo,
        Integer createdById
) {}