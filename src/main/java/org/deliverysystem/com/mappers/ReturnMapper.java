package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ReturnDto;
import org.deliverysystem.com.entities.Return;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReturnMapper extends GenericMapper<Return, ReturnDto> {
    @Override
    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "returnReason.id", target = "returnReasonId")
    ReturnDto toDto(Return entity);

    @Override
    @Mapping(source = "shipmentId", target = "shipment.id")
    @Mapping(source = "returnReasonId", target = "returnReason.id")
    Return toEntity(ReturnDto dto);
}
