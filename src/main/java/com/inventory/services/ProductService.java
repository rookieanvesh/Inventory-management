package com.inventory.services;

import com.inventory.models.Product;
import com.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setQuantityInStock(product.getQuantityInStock());
        
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    public Page<Product> findProducts(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return productRepository.findByNameContainingOrSkuContainingAndActiveTrue(search, search, pageable);
        }
        return productRepository.findByActiveTrue(pageable);
    }
}