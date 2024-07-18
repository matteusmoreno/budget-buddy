package com.matteusmoreno.budget_buddy.product.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotNull(message = "Id is required")
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String image) {
}
