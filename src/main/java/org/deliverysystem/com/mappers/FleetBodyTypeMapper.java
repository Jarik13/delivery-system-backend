package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.FleetBodyTypeDto;
import org.deliverysystem.com.entities.FleetBodyType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FleetBodyTypeMapper extends GenericMapper<FleetBodyType, FleetBodyTypeDto> {
}