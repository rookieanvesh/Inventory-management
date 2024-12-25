package com.inventory.service;

import com.inventory.dtos.CreateOrderDTO;
import com.inventory.dtos.OrderItemDTO;
import com.inventory.dtos.OrderResponseDTO;
import com.inventory.exceptions.InsufficientStockException;
import com.inventory.mapper.OrderMapper;
import com.inventory.models.Order;
import com.inventory.models.Product;
import com.inventory.repositories.OrderRepository;
import com.inventory.repositories.ProductRepository;
import com.inventory.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(2);
        createOrderDTO.setItems(List.of(itemDTO));

        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);
        product.setQuantityInStock(5);

        List<Product> products = List.of(product);
        when(productRepository.findAllById(any())).thenReturn(products);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(orderMapper.toDTO(any(Order.class))).thenReturn(new OrderResponseDTO());

        OrderResponseDTO result = orderService.createOrder(createOrderDTO);
        assertNotNull(result);
    }

    @Test
    void createOrder_InsufficientStock() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(10);
        createOrderDTO.setItems(List.of(itemDTO));

        Product product = new Product();
        product.setId(1L);
        product.setQuantityInStock(5);

        List<Product> products = List.of(product);
        when(productRepository.findAllById(any())).thenReturn(products);

        assertThrows(InsufficientStockException.class, () ->
                orderService.createOrder(createOrderDTO));
    }
}