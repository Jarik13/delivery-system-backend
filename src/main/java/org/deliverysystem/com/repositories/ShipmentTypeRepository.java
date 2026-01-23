package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentTypeRepository extends JpaRepository<ShipmentType, Integer> {
}