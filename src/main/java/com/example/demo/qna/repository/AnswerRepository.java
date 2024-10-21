package com.example.demo.qna.repository;

import com.example.demo.qna.domain.AnswersEntity;
import com.example.demo.qna.domain.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<AnswersEntity,Long> {

    //특정 질문의 공개된 답변만 조회
    List<AnswersEntity> findByQuestionAndIsPublicTrue(QuestionsEntity questionsEntity);

    //특정 질문의 모든 답변 조회(작성자는 비공개 답변도 조회 가능)
    List<AnswersEntity> findByQuestion(QuestionsEntity questionsEntity);
}
