package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.PostomatDto;
import org.deliverysystem.com.entities.Postomat;
import org.deliverysystem.com.mappers.PostomatMapper;
import org.deliverysystem.com.repositories.PostomatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostomatService extends AbstractBaseService<Postomat, PostomatDto, Integer> {
    public PostomatService(PostomatRepository repository, PostomatMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<PostomatDto> findAllByCityId(Integer cityId, Pageable pageable) {
        return ((PostomatRepository) repository).findAllByCityId(cityId, pageable).map(mapper::toDto);
    }
}