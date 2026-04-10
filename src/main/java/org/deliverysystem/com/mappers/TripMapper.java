package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.trips.CoordinateDto;
import org.deliverysystem.com.dtos.trips.TripDto;
import org.deliverysystem.com.dtos.trips.WaybillRefDto;
import org.deliverysystem.com.dtos.trips.WaypointCoordinateDto;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.utils.CityCoordinatesLoader;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.*;

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
    @Mapping(source = "waybillRoutes", target = "waybills", qualifiedByName = "mapWaybills")
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

        List<WaybillRoute> sorted = trip.getWaybillRoutes().stream()
                .sorted(Comparator.comparing(wr -> wr.getSequenceNumber() != null ? wr.getSequenceNumber() : 0))
                .toList();

        return sorted.stream()
                .limit(sorted.size() - 1)
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

    @Named("mapWaybills")
    default List<WaybillRefDto> mapWaybills(List<WaybillRoute> waybillRoutes) {
        if (waybillRoutes == null) return Collections.emptyList();
        return waybillRoutes.stream()
                .filter(wr -> wr.getWaybill() != null)
                .map(wr -> new WaybillRefDto(wr.getWaybill().getId(), wr.getWaybill().getNumber()))
                .toList();
    }

    default CoordinateDto getOriginCoordinates(Trip trip) {
        return resolveCoordinates(resolveOriginCity(trip));
    }

    default CoordinateDto getDestinationCoordinates(Trip trip) {
        return resolveCoordinates(resolveDestinationCity(trip));
    }

    default List<WaypointCoordinateDto> getWaypointCoordinates(Trip trip) {
        if (trip.getWaybillRoutes() == null || trip.getWaybillRoutes().isEmpty()) {
            return List.of();
        }

        List<WaypointCoordinateDto> result = new ArrayList<>();
        Set<Integer> seenCityIds = new LinkedHashSet<>();

        List<WaybillRoute> sortedRoutes = trip.getWaybillRoutes().stream()
                .sorted(Comparator.comparing(wr -> wr.getSequenceNumber() != null ? wr.getSequenceNumber() : 0))
                .toList();

        if (!sortedRoutes.isEmpty()) {
            try {
                var originCity = sortedRoutes.getFirst().getRoute().getOriginBranch().getDeliveryPoint().getCity();
                addCityToResult(originCity, 1, seenCityIds, result);
            } catch (Exception ignored) {}
        }

        for (WaybillRoute wr : sortedRoutes) {
            try {
                var destCity = wr.getRoute().getDestinationBranch().getDeliveryPoint().getCity();
                int seq = (wr.getSequenceNumber() != null ? wr.getSequenceNumber() : 0) + 1;
                addCityToResult(destCity, seq, seenCityIds, result);
            } catch (Exception ignored) {}
        }

        return result;
    }

    private void addCityToResult(City city, Integer seq, Set<Integer> seenIds, List<WaypointCoordinateDto> result) {
        if (city == null || seenIds.contains(city.getId())) {
            return;
        }

        Integer districtId = (city.getDistrict() != null) ? city.getDistrict().getId() : null;
        Integer regionId = (city.getDistrict() != null && city.getDistrict().getRegion() != null)
                ? city.getDistrict().getRegion().getId() : null;

        seenIds.add(city.getId());
        result.add(new WaypointCoordinateDto(
                city.getId(),
                city.getName(),
                districtId,
                regionId,
                city.getLatitude(),
                city.getLongitude(),
                seq
        ));
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