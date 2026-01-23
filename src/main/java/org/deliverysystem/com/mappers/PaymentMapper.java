package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.PaymentDto;
import org.deliverysystem.com.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends GenericMapper<Payment, PaymentDto> {
    @Override
    @Mapping(source = "paymentType.id", target = "paymentTypeId")
    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "amount", target = "amount")
    PaymentDto toDto(Payment entity);

    @Override
    @Mapping(source = "paymentTypeId", target = "paymentType.id")
    @Mapping(source = "shipmentId", target = "shipment.id")
    @Mapping(source = "amount", target = "amount")
    Payment toEntity(PaymentDto dto);
}
