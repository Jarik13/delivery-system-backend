package org.deliverysystem.com.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<D, ID> {
    D findById(ID id);
    Page<D> findAll(Pageable pageable);
    D create(D dto);
    D update(ID id, D dto);
    void delete(ID id);
}
