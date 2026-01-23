package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.StorageCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageConditionRepository extends JpaRepository<StorageCondition, Integer> {
}