package com.tripwhiz.tripwhizadminback.product;

import com.tripwhiz.tripwhizadminback.category.entity.Category;
import com.tripwhiz.tripwhizadminback.category.entity.SubCategory;
import com.tripwhiz.tripwhizadminback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizadminback.category.repository.SubCategoryRepository;
import com.tripwhiz.tripwhizadminback.product.entity.ThemeCategory;
import com.tripwhiz.tripwhizadminback.product.repository.ThemeCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    @Transactional
    @Commit
    public void createData() {
        // Step 1: 카테고리 생성
        List<Category> categories = createCategories();

        // Step 2: 하위 카테고리 생성
        createSubCategories(categories);

        // Step 3: 테마 카테고리 생성
        createThemeCategories();
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "수납/편의", false));
        categories.add(new Category(2L, "의류", false));
        categories.add(new Category(3L, "안전/위생", false));
        categories.add(new Category(4L, "악세사리", false));
        categories.add(new Category(5L, "액티비티 용품", false));
        return categoryRepository.saveAll(categories);
    }

    private void createSubCategories(List<Category> categories) {
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
        subCategoryRepository.saveAll(subCategories);
    }

    private void createThemeCategories() {
        List<ThemeCategory> themeCategories = new ArrayList<>();
        themeCategories.add(new ThemeCategory(null, "Healing", false));
        themeCategories.add(new ThemeCategory(null, "Business", false));
        themeCategories.add(new ThemeCategory(null, "Eating", false));
        themeCategories.add(new ThemeCategory(null, "Shopping", false));
        themeCategories.add(new ThemeCategory(null, "Activity", false));
        themeCategories.add(new ThemeCategory(null, "Culture", false));
        themeCategoryRepository.saveAll(themeCategories);
    }
}
