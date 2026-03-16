package org.deliverysystem.com.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.MinTotalWeight;
import org.deliverysystem.com.dtos.route_lists.CreateRouteListDto;
import org.deliverysystem.com.entities.Shipment;
import org.deliverysystem.com.repositories.ShipmentRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinTotalWeightValidator implements ConstraintValidator<MinTotalWeight, CreateRouteListDto> {
    private final ShipmentRepository shipmentRepository;
    private double minFraction;
    private double maxWeight;

    @Override
    public void initialize(MinTotalWeight annotation) {
        this.minFraction = annotation.minFraction();
        this.maxWeight = annotation.maxWeight();
    }

    @Override
    public boolean isValid(CreateRouteListDto dto, ConstraintValidatorContext ctx) {
        if (dto.shipmentIds() == null || dto.shipmentIds().isEmpty()) return true;

        List<Shipment> shipments = shipmentRepository.findAllById(dto.shipmentIds());
        BigDecimal total = shipments.stream()
                .map(s -> s.getParcel() != null && s.getParcel().getActualWeight() != null
                        ? s.getParcel().getActualWeight()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double min = maxWeight * minFraction;
        if (total.doubleValue() < min) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                            String.format("Загальна вага відправлень (%.2f кг) повинна бути не менше %.0f кг (1/3 від 100 кг)",
                                    total.doubleValue(), min))
                    .addPropertyNode("shipmentIds")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}