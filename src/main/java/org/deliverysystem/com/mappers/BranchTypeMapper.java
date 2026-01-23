package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.BranchTypeDto;
import org.deliverysystem.com.entities.BranchType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchTypeMapper extends GenericMapper<BranchType, BranchTypeDto> {
}