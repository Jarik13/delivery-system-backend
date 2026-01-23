package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.FleetFuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetFuelTypeRepository extends JpaRepository<FleetFuelType, Integer> {
}