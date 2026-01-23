package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.FleetBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetBrandRepository extends JpaRepository<FleetBrand, Integer> {
}