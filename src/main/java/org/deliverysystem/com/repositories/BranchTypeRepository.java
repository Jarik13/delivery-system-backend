package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchTypeRepository extends JpaRepository<BranchType, Integer> {
}