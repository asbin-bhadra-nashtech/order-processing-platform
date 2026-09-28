package com.nashtech.orderapi.service;

import com.nashtech.orderapi.client.OrderProcessorClient;
import com.nashtech.orderapi.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private final OrderProcessorClient orderProcessorClient;

    public OrderService(OrderProcessorClient orderProcessorClient) {
        this.orderProcessorClient = orderProcessorClient;
    }

    public Order createOrder(Order order) {

        order.setId(idGenerator.getAndIncrement());

        orders.add(order);

        orderProcessorClient.processOrder(
                order.getId(),
                order.getCustomerName(),
                order.getProduct(),
                order.getQuantity()
        );

        return order;
    }

    public List<Order> getAllOrders() {
        return List.copyOf(orders);
    }

    public Order getOrderById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}