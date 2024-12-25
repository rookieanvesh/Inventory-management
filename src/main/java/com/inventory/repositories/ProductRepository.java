package com.inventory.repositories;

import com.inventory.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //findById to only get active products
    Optional<Product> findByIdAndActiveTrue(Long id);

    //search to only get active products
    Page<Product> findByNameContainingOrSkuContainingAndActiveTrue(String name, String sku, Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
}