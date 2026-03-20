package org.deliverysystem.com.dtos.employees;

public record EmployeeProfileDto(
        Integer id,
        String firstName,
        String lastName,
        String middleName,
        String phoneNumber,
        BranchInfo branch
) {
    public record BranchInfo(
            Integer id,
            Integer deliveryPointId,
            String name,
            String address,
            Integer cityId
    ) {}
}