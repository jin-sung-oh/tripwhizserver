package com.tripwhiz.tripwhizadminback.category.repository;


import com.tripwhiz.tripwhizadminback.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
