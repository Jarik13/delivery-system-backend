package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.RouteListDto;
import org.deliverysystem.com.entities.RouteList;
import org.deliverysystem.com.mappers.RouteListMapper;
import org.deliverysystem.com.repositories.RouteListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteListService extends AbstractBaseService<RouteList, RouteListDto, Integer> {
    public RouteListService(RouteListRepository repository, RouteListMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<RouteListDto> findAllByCourierId(Integer courierId, Pageable pageable) {
        return ((RouteListRepository) repository).findAllByCourierId(courierId, pageable).map(mapper::toDto);
    }
}