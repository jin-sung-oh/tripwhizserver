package com.example.demo.faq;

import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.domain.FaqCategory;
import com.example.demo.faq.repository.FAQRepository;
import com.example.demo.faq.service.FAQService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@DataJpaTest
@Import(FAQService.class) // FAQService를 테스트 컨텍스트에 명시적으로 추가
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FAQRepositoryTests {

    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private FAQService faqService; // FAQService를 주입받습니다.

    @Test
    @Commit
    @Transactional
    public void testInsert() {
        FAQEntity faq = FAQEntity.builder()
                .question("Test question")
                .answer("Test answer")
                .category(FaqCategory.APP) // FaqCategory enum 사용
                .build();

        faqRepository.save(faq);
        log.info("Inserted FAQ: " + faq);
    }

    // 더미 데이터 생성
    @Test
    @Transactional
    @Commit
    public void insertDummies() {
        // 100개의 질문을 DB에 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            FaqCategory category = FaqCategory.values()[i % FaqCategory.values().length]; // 카테고리 순환

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
        faqRepository.findAll(pageable).forEach(faq -> log.info(faq));
    }

    // FAQ 추가
    @Test
    public void testAddFaq() {
        FaqCategory category = FaqCategory.환불; // 예시로 '환불' 카테고리 사용

        FAQEntity faq = FAQEntity.builder()
                .question("Test question")
                .answer("Test answer")
                .category(category)
                .build();

        FAQEntity savedFaq = faqService.addFaq(faq);
        log.info("Saved FAQ: " + savedFaq);
    }

    // FAQ 수정
//    @Test
//    @Transactional
//    @Commit
//    public void testModifyExistingFAQ() {
//        Long fno = 15L;
//        Optional<FAQEntity> optionalFAQ = faqRepository.findById(fno);
//
//        if (optionalFAQ.isPresent()) {
//            FAQEntity faq = optionalFAQ.get();
//            String updatedQuestion = "Updated question";
//            String updatedAnswer = "Updated answer";
//            FaqCategory updatedCategory = FaqCategory.픽업; // 예시로 '픽업' 카테고리 사용
//
//            faq.updateFields(updatedCategory, updatedQuestion, updatedAnswer);
//            faqRepository.save(faq);
//            log.info("Updated FAQ: " + faq);
//        } else {
//            log.warn("FAQ not found with ID: " + fno);
//        }
//    }

    // FAQ 소프트 삭제
    @Test
    public void testSoftDelete() {
        Long fno = 100L;
        faqService.softDeleteFAQ(fno);
        log.info("Soft deleted FAQ with ID: " + fno);
    }
}
