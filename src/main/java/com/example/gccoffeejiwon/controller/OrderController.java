package com.example.gccoffeejiwon.controller;

import com.example.gccoffeejiwon.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class OrderController {

    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String ordersPage(Model model) {
        var orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    @GetMapping("/order-check/{orderId}")
    public String orderCheckPage(@PathVariable("orderId") UUID orderId, Model model) {
        var items = orderService.getAllOrderItems(orderId);
        model.addAttribute("items", items);
        return "order-check";
    }

    @GetMapping ("/order-check/delete/{orderId}")
    public String orderCancel(@PathVariable("orderId") UUID orderId, Model model) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }


}
