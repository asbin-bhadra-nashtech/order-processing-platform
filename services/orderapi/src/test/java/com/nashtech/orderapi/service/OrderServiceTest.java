package com.nashtech.orderapi.service;

import com.nashtech.orderapi.client.OrderProcessorClient;
import com.nashtech.orderapi.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderService orderService;
    private OrderProcessorClient orderProcessorClient;

    @BeforeEach
    void setUp() {
        orderProcessorClient = mock(OrderProcessorClient.class);

        orderService = new OrderService(orderProcessorClient);
    }

    @Test
    void shouldCreateOrder() {

        Order order = new Order();
        order.setCustomerName("Asbin");
        order.setProduct("Laptop");
        order.setQuantity(1);

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder.getId());
        assertEquals("Asbin", createdOrder.getCustomerName());
        assertEquals("Laptop", createdOrder.getProduct());
        assertEquals(1, createdOrder.getQuantity());

        verify(orderProcessorClient).processOrder(
                createdOrder.getId(),
                "Asbin",
                "Laptop",
                1
        );
    }

    @Test
    void shouldGetAllOrders() {

        Order order = new Order();
        order.setCustomerName("Asbin");
        order.setProduct("Laptop");
        order.setQuantity(1);

        orderService.createOrder(order);

        List<Order> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals("Asbin", orders.get(0).getCustomerName());
    }

    @Test
    void shouldGetOrderById() {

        Order order = new Order();
        order.setCustomerName("Asbin");
        order.setProduct("Laptop");
        order.setQuantity(1);

        Order createdOrder = orderService.createOrder(order);

        Order foundOrder =
                orderService.getOrderById(createdOrder.getId());

        assertNotNull(foundOrder);
        assertEquals(createdOrder.getId(), foundOrder.getId());
    }

    @Test
    void shouldReturnNullWhenOrderDoesNotExist() {

        Order result = orderService.getOrderById(999L);

        assertNull(result);
    }
}