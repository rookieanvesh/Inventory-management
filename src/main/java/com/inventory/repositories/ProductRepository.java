package com.inventory.repositories;

import com.inventory.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingOrSkuContainingAndActiveTrue(String name, String sku, Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
}