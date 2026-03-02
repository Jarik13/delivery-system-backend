package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentBoxRepository extends JpaRepository<ShipmentBox, Integer> {
}
