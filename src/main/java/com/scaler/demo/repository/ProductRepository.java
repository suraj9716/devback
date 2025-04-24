package com.scaler.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scaler.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String>  {

}
