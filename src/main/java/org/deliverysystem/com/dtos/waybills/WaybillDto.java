package org.deliverysystem.com.dtos.waybills;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        Integer createdById,

        @Schema(description = "Ім'я співробітника, що створив", accessMode = Schema.AccessMode.READ_ONLY)
        String createdByName,

        @Schema(description = "Дата створення", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime createdAt,

        @Schema(description = "Кількість відправлень", accessMode = Schema.AccessMode.READ_ONLY)
        Integer shipmentsCount,

        @Schema(description = "Місто відправлення (перший сегмент)", accessMode = Schema.AccessMode.READ_ONLY)
        String originCityName,

        @Schema(description = "Місто призначення (останній сегмент)", accessMode = Schema.AccessMode.READ_ONLY)
        String destinationCityName,

        @Schema(description = "Назва маршруту (напр. 'Київ → Харків → Дніпро')", accessMode = Schema.AccessMode.READ_ONLY)
        String routeSummary,

        @Schema(description = "Загальна відстань маршруту (км)", accessMode = Schema.AccessMode.READ_ONLY)
        BigDecimal totalDistanceKm,

        @Schema(description = "Статус накладної (агрегований зі статусів відправлень)", accessMode = Schema.AccessMode.READ_ONLY)
        String statusSummary,

        @Schema(description = "Кількість доставлених відправлень", accessMode = Schema.AccessMode.READ_ONLY)
        Integer deliveredCount,

        @Schema(description = "Запланована дата відправлення (з прив'язаного рейсу)", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime scheduledDeparture,

        @Schema(description = "Запланована дата прибуття (з прив'язаного рейсу)", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime scheduledArrival,

        @Schema(description = "Номер рейсу до якого прив'язана накладна", accessMode = Schema.AccessMode.READ_ONLY)
        Integer tripNumber,

        @Schema(description = "Список відправлень", accessMode = Schema.AccessMode.READ_ONLY)
        List<WaybillShipmentDto> shipments
) {}