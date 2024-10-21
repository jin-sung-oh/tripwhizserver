package com.example.demo.qna.repository;

import com.example.demo.qna.domain.AnswersEntity;
import com.example.demo.qna.domain.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersRepository extends JpaRepository<AnswersEntity, Long> {

    // 특정 질문의 공개된 답변만 조회 (필드 이름 'questions'에 맞춰 수정)
    List<AnswersEntity> findByQuestionsAndIsPublicTrue(QuestionsEntity questionsEntity);

    // 특정 질문의 모든 답변 조회 (작성자는 비공개 답변도 조회 가능, 필드 이름에 맞춰 수정)
    List<AnswersEntity> findByQuestions(QuestionsEntity questionsEntity);
}
