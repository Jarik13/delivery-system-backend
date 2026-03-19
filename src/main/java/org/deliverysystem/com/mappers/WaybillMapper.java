package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class WaybillMapper implements GenericMapper<Waybill, WaybillDto> {
    @Autowired
    protected WaybillShipmentMapper waybillShipmentMapper;

    @Override
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "createdBy", target = "createdByName", qualifiedByName = "employeeFullName")
    @Mapping(source = "shipmentWaybills", target = "shipmentsCount", qualifiedByName = "mapShipmentsCount")
    @Mapping(source = "shipmentWaybills", target = "shipments", qualifiedByName = "mapShipments")
    @Mapping(source = "shipmentWaybills", target = "deliveredCount", qualifiedByName = "mapDeliveredCount")
    @Mapping(source = "shipmentWaybills", target = "statusSummary", qualifiedByName = "mapStatusSummary")
    @Mapping(source = "waybillRoutes", target = "tripId", qualifiedByName = "mapTripId")
    @Mapping(source = "waybillRoutes", target = "tripNumber", qualifiedByName = "mapTripNumber")
    @Mapping(source = "waybillRoutes", target = "totalDistanceKm", qualifiedByName = "mapTotalDistanceKm")
    @Mapping(source = "waybillRoutes", target = "scheduledDeparture", qualifiedByName = "mapScheduledDeparture")
    @Mapping(source = "waybillRoutes", target = "scheduledArrival", qualifiedByName = "mapScheduledArrival")
    public abstract WaybillDto toDto(Waybill entity);

    @Override
    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "shipmentWaybills", ignore = true)
    @Mapping(target = "waybillRoutes", ignore = true)
    public abstract Waybill toEntity(WaybillDto dto);

    private String resolveOriginCity(Shipment shipment) {
        return Optional.ofNullable(shipment)
                .map(Shipment::getOriginDeliveryPoint)
                .map(ShipmentOriginDeliveryPoint::getDeliveryPoint)
                .map(DeliveryPoint::getCity)
                .map(City::getName)
                .orElse(null);
    }

    private String resolveDestinationCity(Shipment shipment) {
        return Optional.ofNullable(shipment)
                .map(Shipment::getDestinationDeliveryPoint)
                .map(ShipmentDestinationDeliveryPoint::getDeliveryPoint)
                .map(DeliveryPoint::getCity)
                .map(City::getName)
                .orElse(null);
    }

    @Named("employeeFullName")
    String employeeFullName(Employee employee) {
        if (employee == null) return null;
        StringBuilder sb = new StringBuilder();
        if (employee.getLastName() != null) sb.append(employee.getLastName()).append(" ");
        if (employee.getFirstName() != null) sb.append(employee.getFirstName()).append(" ");
        if (employee.getMiddleName() != null) sb.append(employee.getMiddleName());
        return sb.toString().trim();
    }

    @Named("mapShipmentsCount")
    int mapShipmentsCount(List<ShipmentWaybill> shipmentWaybills) {
        return shipmentWaybills != null ? shipmentWaybills.size() : 0;
    }

    @Named("mapShipments")
    List<WaybillShipmentDto> mapShipments(List<ShipmentWaybill> shipmentWaybills) {
        if (shipmentWaybills == null) return Collections.emptyList();
        return shipmentWaybills.stream()
                .filter(sw -> sw.getShipment() != null)
                .map(sw -> waybillShipmentMapper.toDto(sw))
                .sorted(Comparator.comparing(
                        dto -> dto.sequenceNumber() != null ? dto.sequenceNumber() : Integer.MAX_VALUE))
                .toList();
    }

    @Named("mapDeliveredCount")
    int mapDeliveredCount(List<ShipmentWaybill> shipmentWaybills) {
        if (shipmentWaybills == null) return 0;
        return (int) shipmentWaybills.stream()
                .filter(sw -> sw.getShipment() != null)
                .filter(sw -> {
                    var status = sw.getShipment().getShipmentStatus();
                    return status != null && "Доставлено".equalsIgnoreCase(status.getName());
                })
                .count();
    }

    @Named("mapStatusSummary")
    String mapStatusSummary(List<ShipmentWaybill> shipmentWaybills) {
        if (shipmentWaybills == null || shipmentWaybills.isEmpty()) return null;
        long total = shipmentWaybills.stream().filter(sw -> sw.getShipment() != null).count();
        long delivered = shipmentWaybills.stream()
                .filter(sw -> sw.getShipment() != null)
                .filter(sw -> {
                    var status = sw.getShipment().getShipmentStatus();
                    return status != null && "Доставлено".equalsIgnoreCase(status.getName());
                })
                .count();
        if (total == 0) return null;
        if (delivered == total) return "Доставлено";
        if (delivered > 0) return "Частково доставлено";
        return "У дорозі";
    }

    @Named("mapTripId")
    Integer mapTripId(List<WaybillRoute> waybillRoutes) {
        if (waybillRoutes == null || waybillRoutes.isEmpty()) return null;
        return waybillRoutes.stream()
                .filter(wr -> wr.getTrip() != null)
                .findFirst()
                .map(wr -> wr.getTrip().getId())
                .orElse(null);
    }

    @Named("mapTripNumber")
    Integer mapTripNumber(List<WaybillRoute> waybillRoutes) {
        if (waybillRoutes == null || waybillRoutes.isEmpty()) return null;
        return waybillRoutes.stream()
                .filter(wr -> wr.getTrip() != null)
                .findFirst()
                .map(wr -> wr.getTrip().getNumber())
                .orElse(null);
    }

    @Named("mapTotalDistanceKm")
    Float mapTotalDistanceKm(List<WaybillRoute> waybillRoutes) {
        if (waybillRoutes == null || waybillRoutes.isEmpty()) return null;
        double sum = waybillRoutes.stream()
                .filter(wr -> wr.getRoute() != null && wr.getRoute().getDistanceKm() != null)
                .mapToDouble(wr -> wr.getRoute().getDistanceKm())
                .sum();
        return sum > 0 ? (float) sum : null;
    }

    @Named("mapScheduledDeparture")
    LocalDateTime mapScheduledDeparture(List<WaybillRoute> waybillRoutes) {
        if (waybillRoutes == null || waybillRoutes.isEmpty()) return null;
        return waybillRoutes.stream()
                .filter(wr -> wr.getTrip() != null && wr.getTrip().getScheduledDepartureTime() != null)
                .map(wr -> wr.getTrip().getScheduledDepartureTime())
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Named("mapScheduledArrival")
    LocalDateTime mapScheduledArrival(List<WaybillRoute> waybillRoutes) {
        if (waybillRoutes == null || waybillRoutes.isEmpty()) return null;
        return waybillRoutes.stream()
                .filter(wr -> wr.getTrip() != null && wr.getTrip().getScheduledArrivalTime() != null)
                .map(wr -> wr.getTrip().getScheduledArrivalTime())
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }
}