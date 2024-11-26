package com.example.demo.product;

import com.example.demo.category.domain.*;
import com.example.demo.category.repository.CategoryProductRepository;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SubCategoryDummyDataTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUpCategories() {
        // 5개의 Category 더미 데이터를 생성하여 저장합니다.
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Category category = Category.builder()
                    .category(ParentCategory.values()[i % ParentCategory.values().length])
                    .delFlag(false)
                    .themeCategory(ThemeCategory.values()[i % ThemeCategory.values().length])
                    .build();
            categoryRepository.save(category);
        });
    }

    @Test
    void insertDummySubCategories() {
        // 저장된 모든 카테고리를 가져옵니다.
        List<Category> categories = categoryRepository.findAll();

        // 10개의 SubCategory 더미 데이터를 생성하고, 각 SubCategory에 Category를 연결합니다.
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Category category = categories.get(i % categories.size());

            SubCategory subCategory = SubCategory.builder()
                    .sname("서브카테고리 " + i)
                    .category(category) // Category와 연결
                    .build();
            subCategoryRepository.save(subCategory);
        });

        // 데이터 검증
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        assertEquals(10, subCategories.size());

        // 첫 번째 SubCategory의 이름과 연결된 Category 검증
        SubCategory firstSubCategory = subCategories.get(0);
        assertNotNull(firstSubCategory.getCategory());
        assertEquals("서브카테고리 1", firstSubCategory.getSname());
    }

    @Test
    void insertCategoryProductRelations() {
        // 저장된 모든 Category와 Product를 가져옵니다.
        List<Category> categories = categoryRepository.findAll();
        List<Product> products = productRepository.findAll();

        // CategoryProduct 더미 데이터를 생성하고 각 Category와 Product를 연결합니다.
        IntStream.range(0, products.size()).forEach(i -> {
            Category category = categories.get(i % categories.size());
            Product product = products.get(i);

            CategoryProduct categoryProduct = CategoryProduct.builder()
                    .category(category)
                    .product(product)
                    .build();
            categoryProductRepository.save(categoryProduct);
        });

    }
}
