package com.example.demo.category.repository;


import com.example.demo.category.domain.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {


}