package com.matteusmoreno.budget_buddy.address.repository;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByZipcode(String zipcode);

    boolean existsByZipcode(String zipcode);
}
