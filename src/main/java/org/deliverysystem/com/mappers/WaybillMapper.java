package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.WaybillDto;
import org.deliverysystem.com.entities.Waybill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WaybillMapper extends GenericMapper<Waybill, WaybillDto> {
    @Override
    @Mapping(source = "createdBy.id", target = "createdById")
    WaybillDto toDto(Waybill entity);

    @Override
    @Mapping(source = "createdById", target = "createdBy.id")
    Waybill toEntity(WaybillDto dto);
}