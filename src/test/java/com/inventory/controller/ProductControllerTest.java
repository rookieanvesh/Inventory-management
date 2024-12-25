package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.controllers.ProductController;
import com.inventory.dtos.ProductDTO;
import com.inventory.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_Success() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSku("TEST-001");
        productDTO.setName("Test Product");
        productDTO.setPrice(BigDecimal.TEN);
        productDTO.setQuantityInStock(100);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("TEST-001"));
    }

    @Test
    void updateProduct_Success() throws Exception {
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSku("TEST-001");
        productDTO.setName("Updated Product");
        productDTO.setPrice(BigDecimal.TEN);
        productDTO.setQuantityInStock(100);

        when(productService.updateProduct(any(), any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void deleteProduct_Success() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isNoContent());
    }
}