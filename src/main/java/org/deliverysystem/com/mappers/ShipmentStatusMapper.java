package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ShipmentStatusDto;
import org.deliverysystem.com.entities.ShipmentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentStatusMapper extends GenericMapper<ShipmentStatus, ShipmentStatusDto> {
}