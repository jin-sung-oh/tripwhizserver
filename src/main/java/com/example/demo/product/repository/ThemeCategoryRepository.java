package com.example.demo.product.repository;

import com.example.demo.category.domain.Category;
import com.example.demo.product.domain.ThemeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeCategoryRepository extends JpaRepository<ThemeCategory, Long> {

    Optional<ThemeCategory> findByIsDefaultTrue();


}
