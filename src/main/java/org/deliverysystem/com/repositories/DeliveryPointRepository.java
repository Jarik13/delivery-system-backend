package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Integer> {
    Optional<DeliveryPoint> findFirstByCityIdAndBranchIsNotNull(Integer cityId);
}
