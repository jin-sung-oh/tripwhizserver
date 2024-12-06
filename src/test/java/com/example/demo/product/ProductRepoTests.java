package com.example.demo.product;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductTheme;
import com.example.demo.product.domain.ThemeCategory;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.ProductThemeRepository;
import com.example.demo.product.repository.ThemeCategoryRepository;
import com.example.demo.util.file.domain.AttachFile;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepoTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ThemeCategoryRepository themeCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductThemeRepository productThemeRepository;

    @Test
    @Transactional
    @Commit
    public void createDataAndProducts() {
        // Step 1: 카테고리 생성
        List<Category> categories = createCategories();

        // Step 2: 하위 카테고리 생성
        List<SubCategory> subCategories = createSubCategories(categories);

        // Step 3: 테마 카테고리 생성
        List<ThemeCategory> themeCategories = createThemeCategories();

        // Step 4: 상품 생성
        createProducts(categories, subCategories, themeCategories);
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "수납/편의",  false));
        categories.add(new Category(2L, "의류", false));
        categories.add(new Category(3L, "안전/위생", false));
        categories.add(new Category(4L, "악세사리", false));
        categories.add(new Category(5L, "액티비티 용품", false));
        return categoryRepository.saveAll(categories);
    }

    private List<SubCategory> createSubCategories(List<Category> categories) {
        List<SubCategory> subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(null, "파우치", categories.get(0)));
        subCategories.add(new SubCategory(null, "케이스/커버", categories.get(0)));
        subCategories.add(new SubCategory(null, "안대/목베개", categories.get(0)));
        subCategories.add(new SubCategory(null, "와이파이 유심", categories.get(0)));
        subCategories.add(new SubCategory(null, "아우터", categories.get(1)));
        subCategories.add(new SubCategory(null, "상의/하의", categories.get(1)));
        subCategories.add(new SubCategory(null, "잡화", categories.get(1)));
        subCategories.add(new SubCategory(null, "뷰티케어", categories.get(2)));
        subCategories.add(new SubCategory(null, "세면도구", categories.get(2)));
        subCategories.add(new SubCategory(null, "상비약", categories.get(2)));
        subCategories.add(new SubCategory(null, "전기/전자용품", categories.get(3)));
        subCategories.add(new SubCategory(null, "여행 아이템", categories.get(3)));
        subCategories.add(new SubCategory(null, "캠핑/등산", categories.get(4)));
        subCategories.add(new SubCategory(null, "수영", categories.get(4)));
        subCategories.add(new SubCategory(null, "야외/트래킹", categories.get(4)));
        return subCategoryRepository.saveAll(subCategories);
    }

    private List<ThemeCategory> createThemeCategories() {
        List<ThemeCategory> themeCategories = new ArrayList<>();
        themeCategories.add(new ThemeCategory(null, "Healing", false));
        themeCategories.add(new ThemeCategory(null, "Business", false));
        themeCategories.add(new ThemeCategory(null, "Eating", false));
        themeCategories.add(new ThemeCategory(null, "Shopping", false));
        themeCategories.add(new ThemeCategory(null, "Activity", false));
        themeCategories.add(new ThemeCategory(null, "Culture", false));
        return themeCategoryRepository.saveAll(themeCategories);
    }

    private void createProducts(List<Category> categories, List<SubCategory> subCategories, List<ThemeCategory> themeCategories) {
        Random random = new Random();
        final int[] imageCounter = {1}; // 반복문 외부에 선언

        IntStream.rangeClosed(1, 70).forEach(i -> {
            // 랜덤 카테고리 및 하위 카테고리 선택
            Category category = categories.get(random.nextInt(categories.size()));
            List<SubCategory> relatedSubCategories = subCategoryRepository.findByCategory_Cno(category.getCno());
            SubCategory subCategory = relatedSubCategories.get(random.nextInt(relatedSubCategories.size()));

            // 랜덤 테마 선택
            ThemeCategory themeCategory = themeCategories.get(random.nextInt(themeCategories.size()));

            // attachFiles 생성 (builder 사용)
            List<AttachFile> attachFiles = new ArrayList<>();
            for (int j = 0; j < 2; j++) { // 한 상품당 2장의 이미지
                attachFiles.add(AttachFile.builder()
                        .ord(j + 1) // 순서 설정 (1, 2)
                        .filename(imageCounter[0] + ".jpg") // 고유한 파일 이름 설정
                        .build());
                imageCounter[0]++; // 배열로 선언된 값 증가
            }

            // 상품 생성
            Product product = Product.builder()
                    .pname("Product " + i)
                    .price(1000 + random.nextInt(9000)) // 1000 ~ 9999 범위의 랜덤 가격
                    .pdesc("Description for product " + i)
                    .category(category)
                    .subCategory(subCategory)
                    .attachFiles(attachFiles) // attachFiles 추가
                    .build();
            productRepository.save(product);

            // 상품 테마 설정
            ProductTheme productTheme = ProductTheme.builder()
                    .product(product)
                    .themeCategory(themeCategory)
                    .build();

            productThemeRepository.save(productTheme); // 테마 저장
        });
    }



}