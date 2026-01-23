package org.deliverysystem.com.mappers;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface GenericMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
    List<D> toDtoList(List<E> entities);
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}