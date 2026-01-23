package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.PaymentTypeDto;
import org.deliverysystem.com.entities.PaymentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentTypeMapper extends GenericMapper<PaymentType, PaymentTypeDto> {
}