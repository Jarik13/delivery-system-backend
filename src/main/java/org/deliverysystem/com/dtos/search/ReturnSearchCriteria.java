package org.deliverysystem.com.dtos.search;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReturnSearchCriteria(
        String returnTrackingNumber,
        String shipmentTrackingNumber,
        List<Integer> returnReasons,
        LocalDateTime initiationDateFrom,
        LocalDateTime initiationDateTo,
        BigDecimal refundAmountMin,
        BigDecimal refundAmountMax
) {}