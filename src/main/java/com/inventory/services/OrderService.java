package com.inventory.services;

import com.inventory.dtos.CreateOrderDTO;
import com.inventory.dtos.OrderResponseDTO;
import com.inventory.exceptions.InsufficientStockException;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.mapper.OrderMapper;
import com.inventory.models.Order;
import com.inventory.models.OrderItem;
import com.inventory.models.Product;
import com.inventory.repositories.OrderRepository;
import com.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private static final int MAX_RETRIES = 3;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDTO createOrder(CreateOrderDTO orderDTO) {
        //group the items by product ID and sum quantities
        Map<Long, Integer> productQuantities = orderDTO.getItems().stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProductId(),
                        Collectors.summingInt(item -> item.getQuantity())
                ));

        //try to create order with retries in case of concurrent modifications
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return tryCreateOrder(productQuantities);
            } catch (OptimisticLockingFailureException e) {
                if (attempt == MAX_RETRIES - 1) {
                    throw new ConcurrentModificationException(
                            "Unable to place order due to concurrent modifications. Please try again.");
                }
                //Small delay before retry
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Order processing interrupted");
                }
            }
        }
        //This should never be reached due to the throw in the loop
        throw new RuntimeException("Unexpected error in order creation");
    }
    private OrderResponseDTO tryCreateOrder(Map<Long, Integer> productQuantities) {
        //Load all products in one query
        List<Product> products = productRepository.findAllById(productQuantities.keySet());

        if (products.size() != productQuantities.size()) {
            throw new RuntimeException("Some products not found");
        }

        //Verify stock and create order items
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (Product product : products) {
            int requestedQuantity = productQuantities.get(product.getId());
            if (product.getQuantityInStock() < requestedQuantity) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product %s: requested %d, available %d",
                                product.getName(), requestedQuantity, product.getQuantityInStock())
                );
            }
            //Update stock
            product.setQuantityInStock(product.getQuantityInStock() - requestedQuantity);
            //Create order item
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(requestedQuantity);
            item.setPriceAtOrder(product.getPrice());
            items.add(item);

            // Update total
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(requestedQuantity)));
        }

        //Save products with updated stock
        productRepository.saveAll(products);
        //Save order
        order.setItems(items);
        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDTO(savedOrder);
    }
    @Transactional
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toDTO(order);
    }

    @Transactional
    public Page<OrderResponseDTO> listOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDTO);
    }
}
