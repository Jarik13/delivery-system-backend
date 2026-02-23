package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.RouteListStatusDto;
import org.deliverysystem.com.entities.RouteListStatus;
import org.deliverysystem.com.mappers.RouteListStatusMapper;
import org.deliverysystem.com.repositories.RouteListStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class RouteListStatusService extends AbstractBaseService<RouteListStatus, RouteListStatusDto, Integer> {
    public RouteListStatusService(RouteListStatusMapper mapper, RouteListStatusRepository repository) {
        super(mapper, repository);
    }
}
