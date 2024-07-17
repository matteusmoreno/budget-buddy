package com.matteusmoreno.budget_buddy.customer;

import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
