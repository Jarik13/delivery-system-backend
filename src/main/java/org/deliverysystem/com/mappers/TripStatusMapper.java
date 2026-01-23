package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.TripStatusDto;
import org.deliverysystem.com.entities.TripStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripStatusMapper extends GenericMapper<TripStatus, TripStatusDto> {
}