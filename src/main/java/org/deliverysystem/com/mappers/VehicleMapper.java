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
    @Mapping(source = "activityStatus.name", target = "activityStatusName")
    @Mapping(source = "fleet.brand.name", target = "brandName")
    @Mapping(source = "fleet.bodyType.name", target = "bodyTypeName")
    @Mapping(source = "fleet.fuelType.name", target = "fuelTypeName")
    @Mapping(source = "fleet.transmissionType.name", target = "transmissionTypeName")
    @Mapping(source = "fleet.driveType.name", target = "driveTypeName")
    @Mapping(source = "fleet.loadCapacity", target = "loadCapacity")
    @Mapping(source = "fleet.cargoVolume", target = "cargoVolume")
    @Mapping(source = "fleet.engineCapacity", target = "engineCapacity")
    @Mapping(source = "fleet.maxSpeed", target = "maxSpeed")
    VehicleDto toDto(Vehicle entity);

    @Override
    @Mapping(source = "statusId", target = "activityStatus.id")
    @Mapping(source = "fleetId", target = "fleet.id")
    @Mapping(target = "activityStatus.name", ignore = true)
    @Mapping(target = "fleet.brand", ignore = true)
    @Mapping(target = "fleet.bodyType", ignore = true)
    @Mapping(target = "fleet.fuelType", ignore = true)
    @Mapping(target = "fleet.transmissionType", ignore = true)
    @Mapping(target = "fleet.driveType", ignore = true)
    @Mapping(target = "fleet.loadCapacity", ignore = true)
    @Mapping(target = "fleet.cargoVolume", ignore = true)
    @Mapping(target = "fleet.engineCapacity", ignore = true)
    @Mapping(target = "fleet.maxSpeed", ignore = true)
    Vehicle toEntity(VehicleDto dto);
}