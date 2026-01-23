package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.BranchDto;
import org.deliverysystem.com.entities.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper extends GenericMapper<Branch, BranchDto> {
    @Override
    @Mapping(source = "deliveryPoint.name", target = "name")
    @Mapping(source = "deliveryPoint.address", target = "address")
    @Mapping(source = "deliveryPoint.city.id", target = "cityId")
    @Mapping(source = "deliveryPoint.city.name", target = "cityName")
    @Mapping(source = "branchType.id", target = "branchTypeId")
    @Mapping(source = "branchType.name", target = "branchTypeName")
    BranchDto toDto(Branch entity);

    @Override
    @Mapping(source = "name", target = "deliveryPoint.name")
    @Mapping(source = "address", target = "deliveryPoint.address")
    @Mapping(source = "cityId", target = "deliveryPoint.city.id")
    @Mapping(source = "branchTypeId", target = "branchType.id")
    @Mapping(target = "branchType", ignore = true)
    @Mapping(target = "deliveryPoint.city", ignore = true)
    Branch toEntity(BranchDto dto);
}