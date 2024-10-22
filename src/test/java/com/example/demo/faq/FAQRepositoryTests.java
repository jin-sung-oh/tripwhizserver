package com.example.demo.faq;

import com.example.demo.common.domain.CategoryEntity;

import com.example.demo.common.repository.CategoryRepository;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.repository.FAQRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@DataJpaTest
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

    // 추가
    @Test
    public void testAddFaq() {

        Optional<CategoryEntity> categoryOpt = categoryRepository.findById(1);
        CategoryEntity category = categoryOpt.get();

        FAQEntity faq = FAQEntity.builder()
                .question("test")
                .answer("test")
                .category(category)
                .build();

        FAQEntity savedFaq = faqService.addFaq(faq);

        log.info("Saved FAQ: " + savedFaq);

    }

    // 삭제
    @Test
    public void testSoftDelete() {

        Long fno = 100L;

        faqService.softDeleteFAQ(fno);

    }

    @Test
    @Transactional
    @Commit
    public void testModifyExistingFAQ() {
        // 데이터베이스에 있는 임의의 FAQ 엔터티 가져오기 (예: fno가 1인 엔터티)
        Long fno = 5L;
        Optional<FAQEntity> optionalFAQ = faqRepository.findById(fno);

        assertTrue(optionalFAQ.isPresent(), "FAQ가 존재하지 않습니다. fno: " + fno);
        FAQEntity faq = optionalFAQ.get();

        // 수정할 데이터 설정
        String updatedQuestion = "수정된 질문";
        String updatedAnswer = "수정된 답변";
        CategoryEntity updatedCategory = faq.getCategory(); // 기존 카테고리를 유지하거나 변경 가능

        // FAQ 수정
        faq.updateFields(updatedCategory, updatedQuestion, updatedAnswer);
        faqRepository.save(faq);

        // 수정된 데이터 검증
        Optional<FAQEntity> modifiedFAQ = faqRepository.findById(fno);
        assertTrue(modifiedFAQ.isPresent(), "수정된 FAQ를 찾을 수 없습니다.");
        assertEquals(updatedQuestion, modifiedFAQ.get().getQuestion(), "질문이 수정되지 않았습니다.");
        assertEquals(updatedAnswer, modifiedFAQ.get().getAnswer(), "답변이 수정되지 않았습니다.");
    }
}




