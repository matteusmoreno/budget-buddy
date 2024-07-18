package com.matteusmoreno.budget_buddy.product.controller;

import com.matteusmoreno.budget_buddy.product.entity.Product;
import com.matteusmoreno.budget_buddy.product.request.UpdateProductRequest;
import com.matteusmoreno.budget_buddy.product.service.ProductService;
import com.matteusmoreno.budget_buddy.product.request.CreateProductRequest;
import com.matteusmoreno.budget_buddy.product.response.ProductDetailsResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDetailsResponse> create(@RequestBody @Valid CreateProductRequest request, UriComponentsBuilder uriBuilder) {
        Product product = productService.createProduct(request);
        URI uri = uriBuilder.path("products/create/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(uri).body(new ProductDetailsResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponse> productDetails(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }

    @GetMapping("list-all")
    public ResponseEntity<Page<ProductDetailsResponse>> listAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        var page = productService.listAll(pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDetailsResponse> update(@RequestBody @Valid UpdateProductRequest request) {
        Product product = productService.updateProduct(request);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        productService.disableProduct(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<ProductDetailsResponse> enableProduct(@PathVariable Long id) {
        Product product = productService.enableProduct(id);

        return ResponseEntity.ok(new ProductDetailsResponse(product));
    }
}
