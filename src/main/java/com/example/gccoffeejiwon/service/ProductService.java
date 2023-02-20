package com.example.gccoffeejiwon.service;

import com.example.gccoffeejiwon.model.Category;
import com.example.gccoffeejiwon.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductsByCategory(Category category);

    List<Product> getAllProducts();

    Product createProduct(String productName, Category category, long price);
    Product createProduct(String productName, Category category, long price, String description);
}
