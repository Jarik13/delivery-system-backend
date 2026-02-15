package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.TripDto;
import org.deliverysystem.com.entities.Driver;
import org.deliverysystem.com.entities.Trip;
import org.deliverysystem.com.entities.Waybill;
import org.deliverysystem.com.entities.WaybillRoute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface TripMapper extends GenericMapper<Trip, TripDto> {
    @Override
    @Mapping(source = "number", target = "tripNumber")
    @Mapping(source = "status.id", target = "tripStatusId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "status.name", target = "status")
    @Mapping(source = "vehicle.licensePlate", target = "vehiclePlate")
    @Mapping(target = "driverName", source = "driver", qualifiedByName = "formatDriverName")
    @Mapping(target = "originCity", source = "entity", qualifiedByName = "resolveOriginCity")
    @Mapping(target = "destinationCity", source = "entity", qualifiedByName = "resolveDestinationCity")
    @Mapping(target = "shipmentsCount", source = "entity", qualifiedByName = "calculateShipments")
    @Mapping(target = "waypoints", source = "entity", qualifiedByName = "resolveWaypoints")
    @Mapping(target = "totalWeight", source = "entity", qualifiedByName = "calculateTotalWeight")
    @Mapping(target = "distanceKm", source = "entity", qualifiedByName = "calculateTotalDistance")
    TripDto toDto(Trip entity);

    @Override
    @Mapping(source = "tripStatusId", target = "status.id")
    @Mapping(source = "driverId", target = "driver.id")
    @Mapping(source = "vehicleId", target = "vehicle.id")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "waybillRoutes", ignore = true)
    Trip toEntity(TripDto dto);

    @Override
    @Mapping(source = "tripStatusId", target = "status.id")
    @Mapping(source = "driverId", target = "driver.id")
    @Mapping(source = "vehicleId", target = "vehicle.id")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "waybillRoutes", ignore = true)
    void updateEntityFromDto(TripDto dto, @MappingTarget Trip entity);

    @Named("formatDriverName")
    default String formatDriverName(Driver driver) {
        if (driver == null) return "Не вказано";
        return String.format("%s %s %s",
                driver.getLastName() != null ? driver.getLastName() : "",
                driver.getFirstName() != null ? driver.getFirstName() : "",
                driver.getMiddleName() != null ? driver.getMiddleName() : ""
        ).trim().replaceAll("\\s+", " ");
    }

    @Named("resolveOriginCity")
    default String resolveOriginCity(Trip trip) {
        if (trip.getWaybillRoutes() == null || trip.getWaybillRoutes().isEmpty()) return "Не вказано";
        return trip.getWaybillRoutes().get(0).getRoute().getOriginBranch().getDeliveryPoint().getCity().getName();
    }

    @Named("resolveDestinationCity")
    default String resolveDestinationCity(Trip trip) {
        if (trip.getWaybillRoutes() == null || trip.getWaybillRoutes().isEmpty()) return "Не вказано";
        return trip.getWaybillRoutes().getLast().getRoute().getDestinationBranch().getDeliveryPoint().getCity().getName();
    }

    @Named("calculateShipments")
    default Integer calculateShipments(Trip trip) {
        if (trip.getWaybillRoutes() == null) return 0;
        return trip.getWaybillRoutes().stream()
                .filter(wr -> wr.getWaybill() != null && wr.getWaybill().getShipmentWaybills() != null)
                .mapToInt(wr -> wr.getWaybill().getShipmentWaybills().size())
                .sum();
    }

    @Named("resolveWaypoints")
    default List<String> resolveWaypoints(Trip trip) {
        if (trip.getWaybillRoutes() == null) return List.of();
        return trip.getWaybillRoutes().stream()
                .map(wr -> wr.getRoute().getDestinationBranch().getDeliveryPoint().getCity().getName())
                .toList();
    }

    @Named("calculateTotalWeight")
    default BigDecimal calculateTotalWeight(Trip trip) {
        if (trip.getWaybillRoutes() == null) return BigDecimal.ZERO;
        return trip.getWaybillRoutes().stream()
                .map(WaybillRoute::getWaybill)
                .filter(Objects::nonNull)
                .map(Waybill::getTotalWeight)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("calculateTotalDistance")
    default Float calculateTotalDistance(Trip trip) {
        if (trip.getWaybillRoutes() == null) return 0f;
        double total = trip.getWaybillRoutes().stream()
                .map(WaybillRoute::getRoute)
                .filter(Objects::nonNull)
                .mapToDouble(route -> route.getDistanceKm() != null ? route.getDistanceKm() : 0.0)
                .sum();
        return (float) total;
    }
}