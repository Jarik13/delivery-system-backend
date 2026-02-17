package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Integer> {
}
