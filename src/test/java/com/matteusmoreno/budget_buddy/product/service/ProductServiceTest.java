package com.matteusmoreno.budget_buddy.product.service;

import com.matteusmoreno.budget_buddy.exception.ProductNotFoundException;
import com.matteusmoreno.budget_buddy.product.ProductRepository;
import com.matteusmoreno.budget_buddy.product.entity.Product;
import com.matteusmoreno.budget_buddy.product.request.CreateProductRequest;
import com.matteusmoreno.budget_buddy.product.request.UpdateProductRequest;
import com.matteusmoreno.budget_buddy.product.response.ProductDetailsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private final CreateProductRequest createProductRequest = new CreateProductRequest("Product Name", "Description",
            BigDecimal.TEN, 20, "image.png");

    private final Product product = new Product(1L, "Product Name", "Description", BigDecimal.TEN,
            20, "image.png", LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            null, null, true);

    private final UpdateProductRequest updateProductRequest = new UpdateProductRequest(1L, "Updated Product Name",
            "Updated description", null, null, null);

    @Test
    @DisplayName("Should create a new product and save in database")
    void shouldCreateNewProductAndSaveInDatabase() {

        Product result = productService.createProduct(createProductRequest);

        verify(productRepository, times(1)).save(result);

        assertEquals("Product Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals(20, result.getStockQuantity());
        assertEquals("image.png", result.getImage());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should get Product by ID")
    void shouldGetProductById() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        verify(productRepository, times(1)).findById(1L);

        assertEquals("Product Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertEquals(BigDecimal.TEN, result.getPrice());
        assertEquals(20, result.getStockQuantity());
        assertEquals("image.png", result.getImage());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product is not found")
    void shouldThrowProductNotFoundExceptionWhenProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        verify(productRepository, times(1)).findById(1L);

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should list all products with pagination")
    void shouldListAllProductsWithPagination() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDetailsResponse> result = productService.listAll(pageable);

        verify(productRepository, times(1)).findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());

        ProductDetailsResponse response = result.getContent().get(0);
        assertEquals("Product Name", response.name());
        assertEquals("Description", response.description());
        assertEquals(BigDecimal.TEN, response.price());
        assertEquals(20, response.stockQuantity());
        assertEquals("image.png", response.image());
        assertTrue(response.active());
    }

    @Test
    @DisplayName("Should update a Product and save in Database")
    void shouldUpdateProductAndSaveInDatabase() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.updateProduct(updateProductRequest);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(result);

        assertEquals(updateProductRequest.name(), result.getName());
        assertEquals(updateProductRequest.description(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getStockQuantity(), result.getStockQuantity());
        assertEquals(product.getImage(), result.getImage());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should disable product")
    void shouldDisableProduct() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.disableProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);

        assertFalse(product.getActive());
    }

    @Test
    @DisplayName("Should enable product")
    void shouldEnableProduct() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.enableProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(result);

        assertTrue(result.getActive());
    }
}