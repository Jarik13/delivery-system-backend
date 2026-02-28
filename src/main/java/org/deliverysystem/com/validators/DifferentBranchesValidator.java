package org.deliverysystem.com.validators;

import org.deliverysystem.com.annotations.DifferentBranches;
import org.deliverysystem.com.dtos.routes.RouteDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentBranchesValidator implements ConstraintValidator<DifferentBranches, RouteDto> {
    @Override
    public boolean isValid(RouteDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        if (dto.originBranchId() != null && dto.destinationBranchId() != null) {
            boolean isValid = !dto.originBranchId().equals(dto.destinationBranchId());

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("destinationBranchId")
                        .addConstraintViolation();
            }
            return isValid;
        }

        return true;
    }
}
