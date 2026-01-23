package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.FleetTransmissionTypeDto;
import org.deliverysystem.com.entities.FleetTransmissionType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FleetTransmissionTypeMapper extends GenericMapper<FleetTransmissionType, FleetTransmissionTypeDto> {
}