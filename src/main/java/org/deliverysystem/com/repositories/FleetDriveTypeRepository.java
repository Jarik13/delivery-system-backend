package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.FleetDriveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FleetDriveTypeRepository extends JpaRepository<FleetDriveType, Integer> {
}