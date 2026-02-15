package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Критерії для пошуку та фільтрації магістральних рейсів")
public record TripSearchCriteria(
        @Schema(description = "Номер рейсу", example = "101")
        Integer tripNumber,

        @Schema(description = "ID статусу рейсу", example = "1")
        Integer tripStatusId,

        @Schema(description = "ID водія", example = "5")
        Integer driverId,

        @Schema(description = "ID транспортного засобу", example = "12")
        Integer vehicleId,

        @Schema(description = "Плановий час виїзду (від)")
        LocalDateTime scheduledDepartureFrom,

        @Schema(description = "Плановий час виїзду (до)")
        LocalDateTime scheduledDepartureTo,


        @Schema(description = "Фактичний час виїзду (від)")
        LocalDateTime actualDepartureFrom,

        @Schema(description = "Фактичний час виїзду (до)")
        LocalDateTime actualDepartureTo,


        @Schema(description = "Плановий час прибуття (від)")
        LocalDateTime scheduledArrivalFrom,

        @Schema(description = "Плановий час прибуття (до)")
        LocalDateTime scheduledArrivalTo,


        @Schema(description = "Фактичний час прибуття (від)")
        LocalDateTime actualArrivalFrom,

        @Schema(description = "Фактичний час прибуття (до)")
        LocalDateTime actualArrivalTo
) {}