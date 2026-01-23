package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    // Тут зазвичай шукають конкретну адресу, тому складних методів пошуку поки не треба
}