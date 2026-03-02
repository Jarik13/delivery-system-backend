package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Page<Employee> findAllByBranchId(Integer branchId, Pageable pageable);
    Optional<Employee> findByEmail(String email);
}