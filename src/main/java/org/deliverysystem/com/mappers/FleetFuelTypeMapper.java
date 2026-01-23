package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.FleetFuelTypeDto;
import org.deliverysystem.com.entities.FleetFuelType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FleetFuelTypeMapper extends GenericMapper<FleetFuelType, FleetFuelTypeDto> {
}