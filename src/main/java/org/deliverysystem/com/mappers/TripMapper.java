package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.trips.CoordinateDto;
import org.deliverysystem.com.dtos.trips.TripDto;
import org.deliverysystem.com.dtos.trips.WaypointCoordinateDto;
import org.deliverysystem.com.entities.Driver;
import org.deliverysystem.com.entities.Trip;
import org.deliverysystem.com.entities.Waybill;
import org.deliverysystem.com.entities.WaybillRoute;
import org.deliverysystem.com.utils.CityCoordinatesLoader;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
    @Mapping(target = "originCoordinates", expression = "java(getOriginCoordinates(entity))")
    @Mapping(target = "destinationCoordinates", expression = "java(getDestinationCoordinates(entity))")
    @Mapping(target = "waypointCoordinates", expression = "java(getWaypointCoordinates(entity))")
    TripDto toDto(Trip entity);

    @Override
    @Mapping(source = "tripNumber", target = "number")
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
        if (trip.getWaybillRoutes() == null || trip.getWaybillRoutes().isEmpty()) {
            return "Не вказано";
        }

        try {
            return trip.getWaybillRoutes().getFirst()
                    .getRoute()
                    .getOriginBranch()
                    .getDeliveryPoint()
                    .getCity()
                    .getName();
        } catch (Exception e) {
            return "Не вказано";
        }
    }

    @Named("resolveDestinationCity")
    default String resolveDestinationCity(Trip trip) {
        if (trip.getWaybillRoutes() == null || trip.getWaybillRoutes().isEmpty()) {
            return "Не вказано";
        }

        try {
            return trip.getWaybillRoutes().getLast()
                    .getRoute()
                    .getDestinationBranch()
                    .getDeliveryPoint()
                    .getCity()
                    .getName();
        } catch (Exception e) {
            return "Не вказано";
        }
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
        if (trip.getWaybillRoutes() == null || trip.getWaybillRoutes().isEmpty()) {
            return List.of();
        }

        if (trip.getWaybillRoutes().size() <= 1) {
            return List.of();
        }

        return trip.getWaybillRoutes().stream()
                .skip(0)
                .map(wr -> {
                    try {
                        return wr.getRoute()
                                .getDestinationBranch()
                                .getDeliveryPoint()
                                .getCity()
                                .getName();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
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

    default CoordinateDto getOriginCoordinates(Trip trip) {
        return resolveCoordinates(resolveOriginCity(trip));
    }

    default CoordinateDto getDestinationCoordinates(Trip trip) {
        return resolveCoordinates(resolveDestinationCity(trip));
    }

    default List<WaypointCoordinateDto> getWaypointCoordinates(Trip trip) {
        List<String> waypoints = resolveWaypoints(trip);
        if (waypoints == null || waypoints.isEmpty()) {
            return List.of();
        }

        List<WaypointCoordinateDto> result = new ArrayList<>();
        for (String cityName : waypoints) {
            if (cityName == null || cityName.isBlank()) continue;

            double[] coords = CityCoordinatesLoader.getCoordinates(cityName);
            if (coords != null) {
                result.add(new WaypointCoordinateDto(cityName, coords[0], coords[1]));
            }
        }

        return result;
    }

    private CoordinateDto resolveCoordinates(String cityName) {
        if (cityName == null || "Не вказано".equals(cityName)) {
            return null;
        }

        double[] coords = CityCoordinatesLoader.getCoordinates(cityName);
        if (coords == null) {
            return null;
        }

        return new CoordinateDto(coords[0], coords[1]);
    }
}