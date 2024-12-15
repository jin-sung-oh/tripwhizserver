package com.tripwhiz.tripwhizadminback.qna.repository;

import com.tripwhiz.tripwhizadminback.qna.domain.Answers;
import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersRepository extends JpaRepository<Answers, Long> {

    // 특정 질문의 공개된 답변만 조회 (필드 이름 'questions'에 맞춰 수정)
    List<Answers> findByQuestionsAndIsPublicTrue(Questions questionsEntity);

    // 특정 질문의 모든 답변 조회 (작성자는 비공개 답변도 조회 가능, 필드 이름에 맞춰 수정)
    List<Answers> findByQuestions(Questions questionsEntity);
}
