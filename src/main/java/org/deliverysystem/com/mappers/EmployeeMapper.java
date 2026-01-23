package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.EmployeeDto;
import org.deliverysystem.com.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeDto> {
    @Override
    @Mapping(source = "branch.id", target = "branchId")
    EmployeeDto toDto(Employee entity);

    @Override
    @Mapping(source = "branchId", target = "branch.id")
    @Mapping(target = "branch", ignore = true)
    Employee toEntity(EmployeeDto dto);
}