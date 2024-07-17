package com.matteusmoreno.budget_buddy.address.repository;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByZipcodeAndNumber(String zipcode, String number);

    Address findByZipcodeAndNumber(String zipcode, String number);
}
