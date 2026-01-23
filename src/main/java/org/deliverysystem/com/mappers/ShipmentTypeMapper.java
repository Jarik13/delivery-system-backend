package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ShipmentTypeDto;
import org.deliverysystem.com.entities.ShipmentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentTypeMapper extends GenericMapper<ShipmentType, ShipmentTypeDto> {
}