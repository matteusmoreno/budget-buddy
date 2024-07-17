package com.matteusmoreno.budget_buddy.customer.request;

import com.matteusmoreno.budget_buddy.customer.constant.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UpdateCustomerRequest(
        @NotNull(message = "Id is required")
        UUID id,
        String username,
        String password,
        String name,
        @Email(message = "Email is invalid")
        String email,
        @Pattern(regexp = "\\(\\d{2}\\)\\d{9}", message = "Phone is invalid")
        String phone,
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF is invalid")
        String cpf,
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zipcode is invalid")
        String zipcode,
        Country country) {
}
