package com.scaler.demo.controller;
import com.scaler.demo.entity.Product;
import com.scaler.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    @GetMapping("/get")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @GetMapping("/getone")
    public String getDummy() {
        return "This is just a test endpoint.";
    }

    // Add new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productRepository.findById(id).orElse(null);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id);
    }
}