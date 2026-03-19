package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Integer> {
    Optional<ShipmentStatus> findByName(String name);
}