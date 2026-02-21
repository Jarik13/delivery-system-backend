package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.entities.Employee;
import org.deliverysystem.com.entities.ShipmentWaybill;
import org.deliverysystem.com.entities.Waybill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class WaybillMapper implements GenericMapper<Waybill, WaybillDto> {
    @Autowired
    protected WaybillShipmentMapper waybillShipmentMapper;

    @Override
    @Mapping(source = "createdBy.id",     target = "createdById")
    @Mapping(source = "createdAt",        target = "createdAt")
    @Mapping(source = "createdBy",        target = "createdByName",  qualifiedByName = "employeeFullName")
    @Mapping(source = "shipmentWaybills", target = "shipmentsCount", qualifiedByName = "mapShipmentsCount")
    @Mapping(source = "shipmentWaybills", target = "shipments",      qualifiedByName = "mapShipments")
    public abstract WaybillDto toDto(Waybill entity);

    @Override
    @Mapping(source = "createdById",      target = "createdBy.id")
    @Mapping(target = "createdAt",        ignore = true)
    @Mapping(target = "shipmentWaybills", ignore = true)
    @Mapping(target = "waybillRoutes",    ignore = true)
    public abstract Waybill toEntity(WaybillDto dto);

    @Named("employeeFullName")
    String employeeFullName(Employee employee) {
        if (employee == null) return null;
        StringBuilder sb = new StringBuilder();
        if (employee.getLastName()   != null) sb.append(employee.getLastName()).append(" ");
        if (employee.getFirstName()  != null) sb.append(employee.getFirstName()).append(" ");
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
                .map(sw -> waybillShipmentMapper.toDto(sw.getShipment()))
                .toList();
    }
}