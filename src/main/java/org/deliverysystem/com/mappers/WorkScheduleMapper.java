package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.WorkScheduleDto;
import org.deliverysystem.com.entities.WorkSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkScheduleMapper extends GenericMapper<WorkSchedule, WorkScheduleDto> {
    @Override
    @Mapping(source = "dayOfWeek.id", target = "dayOfWeekId")
    @Mapping(source = "workTimeInterval.id", target = "workTimeIntervalId")
    @Mapping(source = "branch.id", target = "branchId")
    WorkScheduleDto toDto(WorkSchedule entity);

    @Override
    @Mapping(source = "dayOfWeekId", target = "dayOfWeek.id")
    @Mapping(source = "workTimeIntervalId", target = "workTimeInterval.id")
    @Mapping(source = "branchId", target = "branch.id")
    WorkSchedule toEntity(WorkScheduleDto dto);
}