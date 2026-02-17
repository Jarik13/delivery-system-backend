package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentDestinationAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentDestinationAddressRepository extends JpaRepository<ShipmentDestinationAddress, Integer> {
}
