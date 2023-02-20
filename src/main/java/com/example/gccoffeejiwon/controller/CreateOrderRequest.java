package com.example.gccoffeejiwon.controller;

import com.example.gccoffeejiwon.model.OrderItem;

import java.util.List;

public record CreateOrderRequest(
        String email, String address, String postcode, List<OrderItem> orderItems
) {
}

