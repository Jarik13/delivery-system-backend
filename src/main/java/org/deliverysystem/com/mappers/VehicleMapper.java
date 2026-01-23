package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.VehicleDto;
import org.deliverysystem.com.entities.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper extends GenericMapper<Vehicle, VehicleDto> {
    @Override
    @Mapping(source = "activityStatus.id", target = "statusId")
    @Mapping(source = "fleet.id", target = "fleetId")
    VehicleDto toDto(Vehicle entity);

    @Override
    @Mapping(source = "statusId", target = "activityStatus.id")
    @Mapping(source = "fleetId", target = "fleet.id")
    Vehicle toEntity(VehicleDto dto);
}