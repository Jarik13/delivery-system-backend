package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.VehicleActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleActivityStatusRepository extends JpaRepository<VehicleActivityStatus, Integer> {
}