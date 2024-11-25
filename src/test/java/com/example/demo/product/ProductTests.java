package com.example.demo.product;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.ParentCategory;
import com.example.demo.category.domain.ThemeCategory;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@DataJpaTest
class ProductTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUpCategories() {
        // 미리 카테고리 데이터 생성 (예: 10개의 카테고리)
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Category category = Category.builder()
                    .category(ParentCategory.values()[i % ParentCategory.values().length])
                    .delFlag(false)
                    .themeCategory(ThemeCategory.values()[i % ThemeCategory.values().length])
                    .build();
            categoryRepository.save(category);
        });
    }

    @Test
    void insertDummyProducts() {
        // 저장된 모든 카테고리를 가져오기
        List<Category> categories = categoryRepository.findAll();

        // 100개의 더미 Product 데이터 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {
            // 카테고리를 순환하여 연결
            Category category = categories.get(i % categories.size());

            Product product = Product.builder()
                    .pname("테스트 상품 " + i)
                    .pdesc("이것은 테스트 상품 설명입니다. 상품 번호: " + i)
                    .price(1000 * i)
                    .delFlag(false)
                    .category(category) // 카테고리 연결
                    .themeCategory(ThemeCategory.values()[i % ThemeCategory.values().length])
                    .build();
            productRepository.save(product);
        });
    }
}
