package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Integer> {
    Optional<Courier> findByKeycloakId(String keycloakId);
}