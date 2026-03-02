package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.BoxVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxVariantRepository extends JpaRepository<BoxVariant, Integer> {
}