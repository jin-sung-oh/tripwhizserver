package com.example.demo.product;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.CategoryProduct;
import com.example.demo.category.domain.ParentCategory;
import com.example.demo.category.domain.ThemeCategory;
import com.example.demo.category.repository.CategoryProductRepository;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepoTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    private final Random random = new Random();

    @Test
    @Transactional
    @Commit
    public void testInsertCategoriesProductsAndCategoryProducts() {
        // 1. 카테고리 10개 생성 및 저장
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Category category = Category.builder()
                    .category(ParentCategory.ALL)
                    .themeCategory(ThemeCategory.RELAXATION)
                    .delFlag(false)
                    .build();
            categoryRepository.save(category);
            log.info("Inserted Category: " + category);
        });

        // 2. 프로덕트 100개 생성 및 동일한 이미지 파일 이름 설정하여 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {

            Product product = Product.builder()
                    .pname("상품 " + i)
                    .pdesc("설명 " + i)
                    .price(1000 * i)
                    .delFlag(false)
                    .themeCategory(ThemeCategory.RELAXATION)
                    .build();
            productRepository.save(product);
            log.info("Inserted Product: " + product);
        });

        // 3. 모든 카테고리와 프로덕트를 조회하여 각각 하나씩 연결
        List<Category> categories = categoryRepository.findAll();
        List<Product> products = productRepository.findAll();

        products.forEach(product -> {
            // Product마다 랜덤한 1개의 Category와 연결
            Category category = categories.get(random.nextInt(categories.size()));

            // CategoryProduct 생성 및 저장
            CategoryProduct categoryProduct = CategoryProduct.builder()
                    .category(category)
                    .product(product)
                    .build();
            categoryProductRepository.save(categoryProduct);

            log.info("Linked Product: " + product.getPname() + " with Category: " + category.getCategory());
        });

        // 4. 검증
        long categoryCount = categoryRepository.count();
        long productCount = productRepository.count();
        long categoryProductCount = categoryProductRepository.count();

        assertThat(categoryCount).isEqualTo(10);
        assertThat(productCount).isEqualTo(100);
        assertThat(categoryProductCount).isEqualTo(100); // Product마다 하나의 Category 연결 확인

        log.info("Total Categories in DB: " + categoryCount);
        log.info("Total Products in DB: " + productCount);
        log.info("Total CategoryProduct entries in DB: " + categoryProductCount);
    }
}
