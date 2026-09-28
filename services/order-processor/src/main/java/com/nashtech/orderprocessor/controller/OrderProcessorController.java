package com.nashtech.orderprocessor.controller;

import com.nashtech.orderprocessor.model.Order;
import com.nashtech.orderprocessor.model.ProcessingResult;
import com.nashtech.orderprocessor.service.OrderProcessorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderProcessorController {

    private final OrderProcessorService orderProcessorService;

    public OrderProcessorController(
            OrderProcessorService orderProcessorService) {
        this.orderProcessorService = orderProcessorService;
    }

    @PostMapping("/process")
    public ProcessingResult processOrder(
            @Valid @RequestBody Order order) {

        return orderProcessorService.processOrder(order);
    }
}