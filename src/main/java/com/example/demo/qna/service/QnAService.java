package com.example.demo.qna.service;

import com.example.demo.qna.domain.AnswersEntity;
import com.example.demo.qna.domain.QuestionsEntity;
import com.example.demo.qna.repository.AnswersRepository;
import com.example.demo.qna.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QnAService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswersRepository answersRepository;  // AnswerRepository -> AnswersRepository로 변경

    // 질문 조회 (작성자는 비공개 질문도 조회 가능)
    public QuestionsEntity getQuestionById(Long qno, String writer) {
        QuestionsEntity questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        // 작성자가 아니고 비공개 상태인 경우 예외 발생
        if (!questionsEntity.isPublic() && !questionsEntity.getWriter().equals(writer)) {
            throw new IllegalArgumentException("작성자만 볼 수 있습니다");
        }

        // 조회수 증가
        questionsEntity.setViewCount(questionsEntity.getViewCount() + 1);
        questionRepository.save(questionsEntity);

        return questionsEntity;
    }

    // 특정 질문에 대한 답변 목록 조회 (작성자는 비공개 답변도 조회 가능)
    public List<AnswersEntity> getAnswersByQuestion(Long qno, String writer) {
        QuestionsEntity questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        // 작성자는 모든 답변을 볼 수 있지만, 작성자가 아닌 경우 비공개 답변을 제외
        if (questionsEntity.getWriter().equals(writer)) {
            return answersRepository.findByQuestions(questionsEntity);  // 필드명에 맞춘 수정
        } else {
            return answersRepository.findByQuestionsAndIsPublicTrue(questionsEntity);  // 필드명에 맞춘 수정
        }
    }
}
