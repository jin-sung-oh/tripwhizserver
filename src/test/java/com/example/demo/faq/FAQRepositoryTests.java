package com.example.demo.faq;

import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.repository.FAQRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FAQRepositoryTests {

    @Autowired
    private FAQRepository faqRepository;

    @Test
    @Transactional
    @Commit
    public void testInsert() {

        FAQEntity FAQ = FAQEntity.builder()
                .fquestion("Test qcontent")
                .fanswer("Test answer")
                .build();

        faqRepository.save(FAQ);

    }


}
