package com.matteusmoreno.budget_buddy.customer.response;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerDetailsResponse(
        UUID id,
        String username,
        String password,
        String name,
        String email,
        String phone,
        String cpf,
        Address address,
        String country,
        BigDecimal balance,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public CustomerDetailsResponse(Customer customer) {
        this(
                customer.getId(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCpf(),
                customer.getAddress(),
                customer.getCountry().getDisplayName(),
                customer.getBalance(),
                customer.getRole().getDisplayName(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt(),
                customer.getActive());
    }
}
