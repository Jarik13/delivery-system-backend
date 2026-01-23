package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ReturnReasonDto;
import org.deliverysystem.com.entities.ReturnReason;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReturnReasonMapper extends GenericMapper<ReturnReason, ReturnReasonDto> {
}