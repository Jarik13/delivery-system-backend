package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByHouseIdAndApartmentNumber(Integer houseId, Integer apartmentNumber);
}