package com.matteusmoreno.budget_buddy.customer.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateCustomerRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid")
        String email,
        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "\\(\\d{2}\\)\\d{9}", message = "Phone is invalid")
        String phone,
        @NotBlank(message = "CPF is required")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF is invalid")
        String cpf,
        @NotBlank(message = "zipcode is required")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zipcode is invalid")
        String zipcode,
        String number,
        @NotNull(message = "Country is required")
        String country) {
}
