package com.example.gccoffeejiwon.controller;

import com.example.gccoffeejiwon.model.Category;

public record CreateProductRequest(String productName, Category category, long price, String description) {
}
