package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ParcelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelTypeRepository extends JpaRepository<ParcelType, Integer> {
}