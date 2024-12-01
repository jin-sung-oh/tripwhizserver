package com.example.demo.product.repository;

import com.example.demo.product.domain.ProductTheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductThemeRepository extends JpaRepository<ProductTheme, Long> {
}
