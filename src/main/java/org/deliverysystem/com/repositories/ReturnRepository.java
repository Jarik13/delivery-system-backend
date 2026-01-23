package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Integer> {
    Optional<Return> findByTrackingNumber(String returnTrackingNumber);
}
