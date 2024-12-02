package com.example.demo.product.repository;

import com.example.demo.product.domain.ThemeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeCategoryRepository extends JpaRepository<ThemeCategory, Long> {
}
