package com.matteusmoreno.budget_buddy.product;

import com.matteusmoreno.budget_buddy.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
