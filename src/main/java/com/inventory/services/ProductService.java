package com.inventory.services;

import com.inventory.dtos.ProductDTO;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.mapper.ProductMapper;
import com.inventory.models.Product;
import com.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product.setActive(true);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        //Update only allowed fields
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantityInStock(productDTO.getQuantityInStock());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional
    public Page<ProductDTO> findProducts(String search, Pageable pageable) {
        Page<Product> productPage;
        if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByNameContainingOrSkuContainingAndActiveTrue(
                    search, search, pageable);
        } else {
            productPage = productRepository.findByActiveTrue(pageable);
        }

        return productPage.map(productMapper::toDTO);
    }

    @Transactional
    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }
}