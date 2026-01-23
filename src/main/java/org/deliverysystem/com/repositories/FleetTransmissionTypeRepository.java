package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.FleetTransmissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetTransmissionTypeRepository extends JpaRepository<FleetTransmissionType, Integer> {
}