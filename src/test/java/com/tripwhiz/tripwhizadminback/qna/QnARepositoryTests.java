package com.tripwhiz.tripwhizadminback.qna;

import com.tripwhiz.tripwhizadminback.qna.domain.Answers;
import com.tripwhiz.tripwhizadminback.qna.domain.QnAStatus;
import com.tripwhiz.tripwhizadminback.qna.domain.QnaCategory;
import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import com.tripwhiz.tripwhizadminback.qna.repository.AnswersRepository;
import com.tripwhiz.tripwhizadminback.qna.repository.QuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

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
            Questions question = Questions.builder()
                    .title("심플 질문 " + i)
                    .qcontent("슈슈슈슈퍼노바 내용 " + i)
                    .writer("작성자 " + i)
                    .isPublic(true)
                    .status(QnAStatus.답변대기)
                    .category(QnaCategory.환불)
                    .createdDate(LocalDateTime.now())
                    .build();
            questionRepository.save(question);

            log.info("Saved Question: " + question);
        });
    }

    @Test
    public void testRead(){
        //상세보기
        Long qno = 60L;

        Optional<Questions> result = questionRepository.findById(qno);

        Questions question = result.orElse(null);

        log.info(question);
    }

    @Test
    @Transactional
    @Commit
    public void testCreateAnswer(){
        //1번 질문에 대한 답변 추가
        Questions questions = questionRepository.findById(1L).orElse(null);

        Answers answers = Answers.builder()
                .acontent("이건 답이다")
                .questions(questions)
                .writer("카리나")
                .isPublic(true)
                .build();
        answersRepository.save(answers);
        log.info("Saved Answers: " + answers);
    }

    @Test
    public void testFindAnswersByQuestion() {
        // 1번 질문에 대한 답변 조회
        Questions question = questionRepository.findById(1L).orElseThrow();
        List<Answers> answers = answersRepository.findByQuestions(question);

        assertThat(answers).isNotEmpty();
        log.info("Found Answers for Question 1: " + answers);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdateQuestion() {
        //1번질문 제목과 상태 변경
        Questions question = questionRepository.findById(1L).orElse(null);

        question.setTitle("업데이트된 질문 제목");

        question.changeStatus(QnAStatus.답변완료);

        questionRepository.save(question);

        Questions updatedQuestion = questionRepository.findById(1L).orElse(null);

        assertThat(updatedQuestion.getTitle()).isEqualTo("업데이트된 질문 제목");

        assertThat(updatedQuestion.getStatus()).isEqualTo(QnAStatus.답변완료);

        log.info("Updated Question: " + updatedQuestion);
    }

    @Test
    @Transactional
    @Commit
    public void testQuestionSearch1() {

        int page = 0;
        int size = 10;

        Pageable pageable =
                PageRequest.of(page,size, Sort.by("qno").descending());

        String keyword = "2";

        Page<Questions> result = questionRepository.findByTitleContaining(keyword, pageable);

        result.get().forEach(questionsEntity -> log.info(questionsEntity));
    }

    @Test
    @Transactional
    @Commit
    public void testQuestionSearch2() {

        int page = 0;
        int size = 10;

        Pageable pageable =
                PageRequest.of(page,size, Sort.by("qno").descending());

        String keyword = "5";

        Page<Questions> result = questionRepository.findByQcontentContaining(keyword, pageable);

        result.get().forEach(questionsEntity -> log.info(questionsEntity));
    }

}
