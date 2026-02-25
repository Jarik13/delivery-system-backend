package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Mapping(source = "shipmentWaybills", target = "originCityName", qualifiedByName = "mapOriginCity")
    @Mapping(source = "shipmentWaybills", target = "destinationCityName", qualifiedByName = "mapDestinationCity")
    @Mapping(source = "shipmentWaybills", target = "routeSummary", qualifiedByName = "mapRouteSummary")
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

    @Named("mapOriginCity")
    String mapOriginCity(List<ShipmentWaybill> shipmentWaybills) {
        if (shipmentWaybills == null || shipmentWaybills.isEmpty()) return null;
        return shipmentWaybills.stream()
                .filter(sw -> sw.getShipment() != null && sw.getSequenceNumber() != null)
                .min(Comparator.comparing(ShipmentWaybill::getSequenceNumber))
                .map(sw -> resolveOriginCity(sw.getShipment()))
                .orElse(null);
    }

    @Named("mapDestinationCity")
    String mapDestinationCity(List<ShipmentWaybill> shipmentWaybills) {
        if (shipmentWaybills == null || shipmentWaybills.isEmpty()) return null;
        return shipmentWaybills.stream()
                .filter(sw -> sw.getShipment() != null && sw.getSequenceNumber() != null)
                .max(Comparator.comparing(ShipmentWaybill::getSequenceNumber))
                .map(sw -> resolveDestinationCity(sw.getShipment()))
                .orElse(null);
    }

    @Named("mapRouteSummary")
    String mapRouteSummary(List<ShipmentWaybill> shipmentWaybills) {
        String origin = mapOriginCity(shipmentWaybills);
        String destination = mapDestinationCity(shipmentWaybills);
        if (origin == null && destination == null) return null;
        if (origin == null) return destination;
        if (destination == null) return origin;
        if (origin.equals(destination)) return origin;
        return origin + " → " + destination;
    }
}