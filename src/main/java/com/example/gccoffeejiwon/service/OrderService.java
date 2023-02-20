package com.example.gccoffeejiwon.service;

import com.example.gccoffeejiwon.model.Email;
import com.example.gccoffeejiwon.model.Order;
import com.example.gccoffeejiwon.model.OrderItem;

import java.util.List;

public interface OrderService {
    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);
}
