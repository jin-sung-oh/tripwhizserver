package com.tripwhiz.tripwhizadminback.faq;

import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.domain.FaqCategory;
import com.example.demo.faq.repository.FAQRepository;
import com.example.demo.faq.service.FAQService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
@Transactional
public class FAQRepositoryTests {

    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private FAQService faqService;

    // 데이터 삽입
    @Test
    @Commit
    public void testInsert() {
        FAQEntity faq = FAQEntity.builder()
                .question("Test question")
                .answer("Test answer")
                .category(FaqCategory.APP)
                .build();

        faqRepository.save(faq);
        log.info("Inserted FAQ: " + faq);
    }

    // 더미 데이터 생성
    @Test
    @Commit
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            FAQEntity faq = FAQEntity.builder()
                    .question("Question " + i)
                    .answer("Answer " + i)
                    .viewCnt(0)
                    .delFlag(false)
                    .category(FaqCategory.매장)
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

    // FAQ 조회
    @Test
    public void testRead() {

        Long fno = 100L;

        log.info(faqRepository.read(fno));

    }

    // 데이터 추가
    @Test
    public void testAddFaq() {


        FAQEntity faq = FAQEntity.builder()
                .question("test")
                .answer("test")
                .category(FaqCategory.APP)
                .build();

    }

    // 데이터 수정
    @Test
    @Commit
    public void testModifyExistingFAQ() {
        Long fno = 15L;
        Optional<FAQEntity> optionalFAQ = faqRepository.findById(fno);

        if (optionalFAQ.isPresent()) {
            FAQEntity faq = optionalFAQ.get();
            faq.updateFields(FaqCategory.픽업, "Updated question", "Updated answer");
            faqRepository.save(faq);
            log.info("Updated FAQ: " + faq);
        } else {
            log.warn("FAQ not found with ID: " + fno);
        }
    }

    // 데이터 삭제
    @Test
    public void testSoftDelete() {
        Long fno = 100L;
        faqService.softDeleteFAQ(fno);
        log.info("Soft deleted FAQ with ID: " + fno);
    }
}
