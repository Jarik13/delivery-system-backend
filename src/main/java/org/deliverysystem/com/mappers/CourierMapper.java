package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.CourierDto;
import org.deliverysystem.com.entities.Courier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourierMapper extends GenericMapper<Courier, CourierDto> {
}
