package com.example.demo.category.controller;

import com.example.demo.category.dto.CategoryDTO;
import com.example.demo.category.dto.SubCategoryDTO;
import com.example.demo.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Log4j2
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {

        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{cno}/subcategories")
    public ResponseEntity<List<SubCategoryDTO>> getSubCategories(@PathVariable Long cno) {

        List<SubCategoryDTO> subCategories = categoryService.getSubCategoriesByCategory(cno);
        return ResponseEntity.ok(subCategories);
    }
}
