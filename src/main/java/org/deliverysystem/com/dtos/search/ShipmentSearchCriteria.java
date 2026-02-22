package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Критерії для динамічного пошуку та фільтрації відправлень")
public record ShipmentSearchCriteria(
        @Schema(description = "Пошук за трек-номером (частковий збіг)", example = "59000")
        String trackingNumber,

        @Schema(description = "ID статусу відправлення", example = "1")
        List<Integer> shipmentStatuses,

        @Schema(description = "ID типу відправлення (напр. Стандарт, Експрес)", example = "2")
        List<Integer> shipmentTypes,

        @Schema(description = "Пошук в описі вмісту посилки (частковий збіг)", example = "Фен")
        String parcelDescription,

        @Schema(description = "Дата оформлення (створення) від")
        LocalDateTime createdAtFrom,

        @Schema(description = "Дата оформлення (створення) до")
        LocalDateTime createdAtTo,

        @Schema(description = "Дата видачі отримувачу від")
        LocalDateTime issuedAtFrom,

        @Schema(description = "Дата видачі отримувачу до")
        LocalDateTime issuedAtTo,

        @Schema(description = "Мінімальна вага вантажу (кг)", example = "0.5")
        BigDecimal weightMin,

        @Schema(description = "Максимальна вага вантажу (кг)", example = "30.0")
        BigDecimal weightMax,

        @Schema(description = "Мінімальна загальна вартість до сплати (грн)")
        BigDecimal totalPriceMin,

        @Schema(description = "Максимальна загальна вартість до сплати (грн)")
        BigDecimal totalPriceMax,

        @Schema(description = "Мінімальний базовий тариф доставки")
        BigDecimal deliveryPriceMin,

        @Schema(description = "Максимальний базовий тариф доставки")
        BigDecimal deliveryPriceMax,

        @Schema(description = "Мінімальна доплата за вагу")
        BigDecimal weightPriceMin,

        @Schema(description = "Максимальна доплата за вагу")
        BigDecimal weightPriceMax,

        @Schema(description = "Мінімальна доплата за відстань")
        BigDecimal distancePriceMin,

        @Schema(description = "Максимальна доплата за відстань")
        BigDecimal distancePriceMax,

        @Schema(description = "Мінімальна ціна варіанту коробки")
        BigDecimal boxVariantPriceMin,

        @Schema(description = "Максимальна ціна варіанту коробки")
        BigDecimal boxVariantPriceMax,

        @Schema(description = "Мінімальна ціна спеціального пакування")
        BigDecimal specialPackagingPriceMin,

        @Schema(description = "Максимальна ціна спеціального пакування")
        BigDecimal specialPackagingPriceMax,

        @Schema(description = "Мінімальний страховий збір")
        BigDecimal insuranceFeeMin,

        @Schema(description = "Максимальний страховий збір")
        BigDecimal insuranceFeeMax
) {}