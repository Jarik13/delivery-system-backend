package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {
    Optional<SuperAdmin> findByKeycloakId(String keycloakId);
}