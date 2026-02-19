package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>, JpaSpecificationExecutor<City> {
    Page<City> findAllByDistrictId(Integer districtId, Pageable pageable);
}