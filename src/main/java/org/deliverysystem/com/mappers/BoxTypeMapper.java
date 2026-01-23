package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.BoxTypeDto;
import org.deliverysystem.com.entities.BoxType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoxTypeMapper extends GenericMapper<BoxType, BoxTypeDto> {
}