package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.VehicleActivityStatusDto;
import org.deliverysystem.com.entities.VehicleActivityStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleActivityStatusMapper extends GenericMapper<VehicleActivityStatus, VehicleActivityStatusDto> {
}