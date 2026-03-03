package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.entities.Return;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReturnMapper extends GenericMapper<Return, ReturnDto> {
    @Override
    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "shipment.trackingNumber", target = "shipmentTrackingNumber")
    @Mapping(source = "returnReason.id", target = "returnReasonId")
    @Mapping(source = "returnReason.name", target = "returnReasonName")
    @Mapping(source = "trackingNumber", target = "returnTrackingNumber")
    ReturnDto toDto(Return entity);

    @Override
    @Mapping(source = "shipmentId", target = "shipment.id")
    @Mapping(source = "returnReasonId", target = "returnReason.id")
    @Mapping(source = "returnTrackingNumber", target = "trackingNumber")
    Return toEntity(ReturnDto dto);
}