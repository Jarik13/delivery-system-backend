package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Page<District> findAllByRegionId(Integer regionId, Pageable pageable);
}