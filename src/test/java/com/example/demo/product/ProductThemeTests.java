package com.example.demo.product;

import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductTheme;
import com.example.demo.product.domain.ThemeCategory;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.ProductThemeRepository;
import com.example.demo.product.repository.ThemeCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductThemeTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ThemeCategoryRepository themeCategoryRepository;

    @Autowired
    private ProductThemeRepository productThemeRepository;

    @Test
    @Transactional
    @Commit
    public void connectProductWithThemeCategories(){

        // 1. Product와 ThemeCategory 조회
        List<Product> products = productRepository.findAll();
        List<ThemeCategory> themeCategories = themeCategoryRepository.findAll();

        // 2. Product와 ThemeCategory 연결
        Random random = new Random();
        for (Product product : products) {
            for (int i = 0; i < 2; i++) { // 각 Product에 2개의 ThemeCategory 연결
                ThemeCategory themeCategory = themeCategories.get(random.nextInt(themeCategories.size()));

                // 중간 테이블 엔티티 생성
                ProductTheme productTheme = ProductTheme.builder()
                        .product(product)
                        .themeCategory(themeCategory)
                        .build();

                // 중간 테이블 엔티티 저장
                productThemeRepository.save(productTheme);

                log.info("ProductTheme created: {}", productTheme);
            }
        }

        log.info("Product와 ThemeCategory의 관계가 성공적으로 생성되었습니다.");
    }


}
