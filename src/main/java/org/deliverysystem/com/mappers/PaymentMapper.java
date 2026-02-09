package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends GenericMapper<Payment, PaymentDto> {
    @Override
    @Mapping(source = "paymentType.id", target = "paymentTypeId")
    @Mapping(source = "paymentType.name", target = "paymentTypeName")
    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "date", target = "paymentDate")
    PaymentDto toDto(Payment entity);

    @Override
    @Mapping(source = "paymentTypeId", target = "paymentType.id")
    @Mapping(source = "shipmentId", target = "shipment.id")
    @Mapping(source = "paymentDate", target = "date")
    Payment toEntity(PaymentDto dto);
}
