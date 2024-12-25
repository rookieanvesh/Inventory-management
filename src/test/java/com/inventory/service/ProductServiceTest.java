package com.inventory.service;

import com.inventory.dtos.ProductDTO;
import com.inventory.mapper.ProductMapper;
import com.inventory.models.Product;
import com.inventory.repositories.ProductRepository;
import com.inventory.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_Success() {
        ProductDTO inputDto = new ProductDTO();
        inputDto.setSku("TEST-001");
        inputDto.setName("Test Product");
        inputDto.setPrice(BigDecimal.TEN);
        inputDto.setQuantityInStock(100);

        Product product = new Product();
        product.setId(1L);
        product.setSku("TEST-001");

        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(inputDto);

        ProductDTO result = productService.createProduct(inputDto);

        assertNotNull(result);
        assertEquals("TEST-001", result.getSku());
    }

    @Test
    void updateProduct_Success() {
        Long productId = 1L;
        ProductDTO updateDto = new ProductDTO();
        updateDto.setName("Updated Product");
        updateDto.setPrice(BigDecimal.valueOf(20));

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findByIdAndActiveTrue(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(productMapper.toDTO(any(Product.class))).thenReturn(updateDto);

        ProductDTO result = productService.updateProduct(productId, updateDto);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
    }

    @Test
    void findProducts_Success() {
        Pageable pageable = Pageable.unpaged();
        List<Product> products = List.of(new Product());
        Page<Product> productPage = new PageImpl<>(products);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");

        when(productRepository.findByActiveTrue(pageable)).thenReturn(productPage);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.findProducts(null, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
