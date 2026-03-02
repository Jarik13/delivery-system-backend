package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentOriginAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentOriginAddressRepository extends JpaRepository<ShipmentOriginAddress, Integer> {
}
