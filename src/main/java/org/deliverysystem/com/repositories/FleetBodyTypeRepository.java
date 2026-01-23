package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.FleetBodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetBodyTypeRepository extends JpaRepository<FleetBodyType, Integer> {
}