package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Street;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepository extends JpaRepository<Street, Integer> {
    Page<Street> findAllByCityId(Integer cityId, Pageable pageable);
}