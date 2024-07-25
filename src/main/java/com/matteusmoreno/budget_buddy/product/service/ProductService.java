package com.matteusmoreno.budget_buddy.product.service;

import com.matteusmoreno.budget_buddy.exception.ProductNotFoundException;
import com.matteusmoreno.budget_buddy.product.entity.Product;
import com.matteusmoreno.budget_buddy.product.ProductRepository;
import com.matteusmoreno.budget_buddy.product.request.CreateProductRequest;
import com.matteusmoreno.budget_buddy.product.request.UpdateProductRequest;
import com.matteusmoreno.budget_buddy.product.response.ProductDetailsResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);

        product.setName(request.name().toUpperCase());
        product.setActive(true);

        productRepository.save(product);

        return product;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public Page<ProductDetailsResponse> listAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDetailsResponse::new);
    }

    @Transactional
    public Product updateProduct(UpdateProductRequest request) {
        Product product = productRepository.findById(request.id()).orElseThrow(ProductNotFoundException::new);

        if (request.name() != null) {
            product.setName(request.name().toUpperCase());
        }
        if (request.description() != null) {
            product.setDescription(request.description());
        }
        if (request.price() != null) {
            product.setPrice(request.price());
        }
        if (request.image() != null) {
            product.setImage(request.image());
        }

        productRepository.save(product);

        return product;
    }

    @Transactional
    public void disableProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setActive(false);
        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    @Transactional
    public Product enableProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setActive(true);
        product.setDeletedAt(null);

        productRepository.save(product);

        return product;
    }
}
