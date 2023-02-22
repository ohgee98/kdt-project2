package com.example.gccoffeejiwon.service;

import com.example.gccoffeejiwon.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);

    List<OrderVO> getAllOrders();

    List<OrderItemVO> getAllOrderItems(UUID orderId);

    void deleteOrder(UUID orderId);

}
