package org.deliverysystem.com.dtos.search;

import java.math.BigDecimal;
import java.util.List;

public record RouteListSearchCriteria(
        Integer number,
        BigDecimal totalWeightMin,
        BigDecimal totalWeightMax,
        Integer courierId,
        List<Integer> statuses
) {}