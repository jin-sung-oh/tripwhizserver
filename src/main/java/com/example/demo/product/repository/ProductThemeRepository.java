package com.example.demo.product.repository;

import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductThemeRepository extends JpaRepository<ProductTheme, Long> {



}
