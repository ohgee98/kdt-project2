package com.example.gccoffeejiwon.controller.api;

import com.example.gccoffeejiwon.controller.CreateOrderRequest;
import com.example.gccoffeejiwon.model.*;
import com.example.gccoffeejiwon.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/orders")
    public Order createOrder(@RequestBody CreateOrderRequest orderRequest) {
        return orderService.createOrder(
                new Email(orderRequest.email()),
                orderRequest.address(),
                orderRequest.postcode(),
                orderRequest.orderItems()
        );
    }
}
