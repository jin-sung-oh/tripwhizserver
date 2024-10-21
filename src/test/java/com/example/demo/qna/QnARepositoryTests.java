package com.example.demo.qna;

import com.example.demo.qna.domain.AnswersEntity;
import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.domain.QuestionsEntity;
import com.example.demo.qna.repository.AnswersRepository;
import com.example.demo.qna.repository.QuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnARepositoryTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswersRepository answersRepository;

    @Test
    @Transactional
    @Commit
    public void insertDummies() {
        // 100개의 질문을 DB에 저장
        IntStream.rangeClosed(1, 100).forEach(i -> {
            QuestionsEntity question = QuestionsEntity.builder()
                    .title("심플 질문 " + i)
                    .question("슈슈슈슈퍼노바 내용 " + i)
                    .writer("작성자 " + i)
                    .isPublic(true)
                    .status(QnAStatus.답변대기)
                    .build();
            questionRepository.save(question);

            log.info("Saved Question: " + question);
        });
    }

    @Test
    public void testRead(){
        //상세보기
        Long qno = 60L;

        Optional<QuestionsEntity> result = questionRepository.findById(qno);

        QuestionsEntity question = result.orElse(null);

        log.info(question);
    }

    @Test
    @Transactional
    @Commit
    public void testCreateAnswer(){
        //1번 질문에 대한 답변 추가
        QuestionsEntity questions = questionRepository.findById(1L).orElse(null);

        AnswersEntity answers = AnswersEntity.builder()
                .answers("이건 답이다")
                .questions(questions)
                .writer("카리나")
                .isPublic(true)
                .build();
        answersRepository.save(answers);
        log.info("Saved Answers: " + answers);
    }





}
