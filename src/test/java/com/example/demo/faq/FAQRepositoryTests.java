package com.example.demo.faq;

import com.example.demo.common.domain.CategoryEntity;
import com.example.demo.common.repository.CategoryRepository;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.repository.FAQRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FAQRepositoryTests {

    @Autowired
    private FAQRepository faqRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Commit
    @Transactional
    public void testInsert() {

        FAQEntity FAQ = FAQEntity.builder()
                .question("Test question")
                .answer("Test answer")
                .build();

        faqRepository.save(FAQ);

    }

    @Test
    @Commit
    @Disabled
    @Transactional
    public void insertDummies() {

        List<CategoryEntity> categories = categoryRepository.findAll();

        IntStream.rangeClosed(1, 100).forEach(i -> {

            CategoryEntity category = categories.get((i - 1) % categories.size());

            FAQEntity faq = FAQEntity.builder()
                    .question("Test question " + i)
                    .answer("Test answer " + i)
                    .category(category)
                    .build();

            faqRepository.save(faq);

        });

    }


}
