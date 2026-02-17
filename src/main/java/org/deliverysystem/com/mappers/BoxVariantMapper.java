package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.BoxVariantDto;
import org.deliverysystem.com.entities.BoxVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoxVariantMapper extends GenericMapper<BoxVariant, BoxVariantDto> {
    @Override
    @Mapping(source = "boxType.id", target = "boxTypeId")
    @Mapping(source = "boxType.name", target = "boxTypeName")
    BoxVariantDto toDto(BoxVariant entity);

    @Override
    @Mapping(source = "boxTypeId", target = "boxType.id")
    BoxVariant toEntity(BoxVariantDto dto);
}