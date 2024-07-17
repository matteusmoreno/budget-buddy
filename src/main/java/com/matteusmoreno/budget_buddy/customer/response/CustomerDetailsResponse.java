package com.matteusmoreno.budget_buddy.customer.response;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;

import java.time.LocalDateTime;

public record CustomerDetailsResponse(
        String username,
        String password,
        String name,
        String email,
        String phone,
        String cpf,
        Address address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public CustomerDetailsResponse(Customer customer) {
        this(customer.getUsername(), customer.getPassword(), customer.getName(), customer.getEmail(), customer.getPhone(),
                customer.getCpf(), customer.getAddress(), customer.getCreatedAt(), customer.getUpdatedAt(), customer.getDeletedAt(),
                customer.getActive());
    }
}
