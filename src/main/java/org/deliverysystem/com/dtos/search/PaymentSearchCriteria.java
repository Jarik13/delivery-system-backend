package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Критерії для динамічного пошуку та фільтрації платежів")
public record PaymentSearchCriteria(
        @Schema(description = "Пошук за номером транзакції (частковий збіг)", example = "TXN-999")
        String transactionNumber,

        @Schema(description = "Пошук за трек-номером відправлення (частковий збіг)", example = "59000")
        String shipmentTrackingNumber,

        @Schema(description = "ID відправлення", example = "100")
        Integer shipmentId,

        @Schema(description = "ID типу оплати (Готівка / Карта тощо)")
        List<Integer> paymentTypes,

        @Schema(description = "Мінімальна сума платежу (грн)", example = "50.00")
        BigDecimal amountMin,

        @Schema(description = "Максимальна сума платежу (грн)", example = "5000.00")
        BigDecimal amountMax,

        @Schema(description = "Дата оплати від")
        LocalDateTime paymentDateFrom,

        @Schema(description = "Дата оплати до")
        LocalDateTime paymentDateTo
) {}