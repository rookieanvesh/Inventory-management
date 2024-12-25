package com.inventory.mapper;

import com.inventory.dtos.OrderItemResponseDTO;
import com.inventory.dtos.OrderResponseDTO;
import com.inventory.models.Order;
import com.inventory.models.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    public OrderResponseDTO toDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());

        dto.setItems(order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }
    private OrderItemResponseDTO toItemDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtOrder(item.getPriceAtOrder());
        dto.setSubtotal(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }
}