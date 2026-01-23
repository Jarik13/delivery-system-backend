package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ReturnReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnReasonRepository extends JpaRepository<ReturnReason, Integer> {
}