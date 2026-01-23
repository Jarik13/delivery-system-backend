package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.TripDto;
import org.deliverysystem.com.entities.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper extends GenericMapper<Trip, TripDto> {
    @Override
    @Mapping(source = "status.id", target = "tripStatusId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    TripDto toDto(Trip entity);

    @Override
    @Mapping(source = "tripStatusId", target = "status.id")
    @Mapping(source = "driverId", target = "driver.id")
    @Mapping(source = "vehicleId", target = "vehicle.id")
    Trip toEntity(TripDto dto);
}