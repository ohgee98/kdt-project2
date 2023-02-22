package com.example.gccoffeejiwon.repository;

import com.example.gccoffeejiwon.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order insert(Order order);

    List<OrderVO> findAll();

    Optional<OrderVO> findById(UUID orderId);

    Optional<OrderVO> findByEmail(Email email);

    List<OrderItemVO> findAllItems(UUID orderId);

    void deleteAll();

    void deleteOrder(UUID orderId);


}

