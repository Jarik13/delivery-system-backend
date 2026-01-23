package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.FleetDriveTypeDto;
import org.deliverysystem.com.entities.FleetDriveType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FleetDriveTypeMapper extends GenericMapper<FleetDriveType, FleetDriveTypeDto> {
}