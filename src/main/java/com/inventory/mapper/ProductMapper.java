package com.inventory.mapper;

import com.inventory.dtos.ProductDTO;
import com.inventory.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantityInStock(product.getQuantityInStock());
        return dto;
    }
    
    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantityInStock(dto.getQuantityInStock());
        return product;
    }
}