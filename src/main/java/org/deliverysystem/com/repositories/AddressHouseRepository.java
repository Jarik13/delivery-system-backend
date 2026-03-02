package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.AddressHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressHouseRepository extends JpaRepository<AddressHouse, Integer> {
    Page<AddressHouse> findAllByStreetId(Integer streetId, Pageable pageable);
    Optional<AddressHouse> findByStreetIdAndNumber(Integer streetId, String number);
}