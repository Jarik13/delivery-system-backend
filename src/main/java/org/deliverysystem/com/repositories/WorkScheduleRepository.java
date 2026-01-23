package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.WorkSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {
    Page<WorkSchedule> findAllByBranchId(Integer branchId, Pageable pageable);
}