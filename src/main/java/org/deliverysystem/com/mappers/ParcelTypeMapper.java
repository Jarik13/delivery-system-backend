package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ParcelTypeDto;
import org.deliverysystem.com.entities.ParcelType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParcelTypeMapper extends GenericMapper<ParcelType, ParcelTypeDto> {
}