package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.controllers.OrderController;
import com.inventory.dtos.CreateOrderDTO;
import com.inventory.dtos.OrderItemDTO;
import com.inventory.dtos.OrderResponseDTO;
import com.inventory.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_Success() throws Exception {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(2);
        createOrderDTO.setItems(List.of(itemDTO));

        OrderResponseDTO responseDTO = new OrderResponseDTO();

        when(orderService.createOrder(any(CreateOrderDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void getOrder_Success() throws Exception {
        Long orderId = 1L;
        OrderResponseDTO responseDTO = new OrderResponseDTO();

        when(orderService.getOrder(orderId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk());
    }
}