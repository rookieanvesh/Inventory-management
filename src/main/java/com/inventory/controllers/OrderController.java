package com.inventory.controllers;

import com.inventory.dtos.CreateOrderDTO;
import com.inventory.dtos.OrderResponseDTO;
import com.inventory.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> listOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.listOrders(pageable));
    }
}