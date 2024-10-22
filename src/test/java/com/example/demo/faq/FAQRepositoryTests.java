package com.example.demo.faq;

import com.example.demo.common.domain.CategoryEntity;

import com.example.demo.common.repository.CategoryRepository;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.repository.FAQRepository;
import com.example.demo.faq.service.FAQService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FAQRepositoryTests {

    @Autowired
    private FAQRepository faqRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FAQService faqService;

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

    // 더미 데이터 생성
    @Test
    @Transactional
    @Commit
    public void insertDummies() {

        List<CategoryEntity> categories = categoryRepository.findAll();

        // 100개의 질문을 DB에 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {

            CategoryEntity category = categories.get((i - 1) % categories.size());

            FAQEntity faq = FAQEntity.builder()
                    .question("Question " + i)
                    .answer("Answer " + i)
                    .viewCnt(0)
                    .delFlag(false)
                    .category(category)
                    .build();
            faqRepository.save(faq);

            log.info("Saved FAQ: " + faq);
        });
    }

    // 리스트 조회
    @Test
    public void testList() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("fno").descending());

        faqRepository.findAll(pageable);
//        faqService.list(pageable);

    }

    // 삭제
    @Test
    public void testSoftDelete() {

        Long fno = 100L;

        faqService.softDeleteFAQ(fno);

    }


}
