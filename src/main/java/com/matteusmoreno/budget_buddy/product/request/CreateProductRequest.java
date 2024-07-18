package com.matteusmoreno.budget_buddy.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Price is required")
        BigDecimal price,
        @NotNull(message = "Stock Quantity is required")
        Integer stockQuantity,
        @NotBlank(message = "Image is required")
        String image) {
}
