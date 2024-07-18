package com.matteusmoreno.budget_buddy.product.response;

import com.matteusmoreno.budget_buddy.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public ProductDetailsResponse(Product product) {
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStockQuantity(),
                product.getImage(), product.getCreatedAt(), product.getUpdatedAt(), product.getDeletedAt(), product.getActive());
    }
}
