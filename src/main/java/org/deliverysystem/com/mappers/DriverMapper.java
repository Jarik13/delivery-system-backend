package org.deliverysystem.com.mappers;
import org.deliverysystem.com.dtos.DriverDto;
import org.deliverysystem.com.entities.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper extends GenericMapper<Driver, DriverDto> {
    @Override
    @Mapping(target = "hasActiveTrip", ignore = true)
    DriverDto toDto(Driver entity);
}