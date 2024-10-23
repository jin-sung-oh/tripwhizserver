package com.example.demo.qna.service;


import com.example.demo.qna.domain.AnswersEntity;
import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.domain.QuestionsEntity;
import com.example.demo.qna.dto.AnswerDTO;
import com.example.demo.qna.repository.AnswersRepository;
import com.example.demo.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Transactional
public class AnswersService {

    private final AnswersRepository answersRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AnswersService(AnswersRepository answersRepository, QuestionRepository questionRepository) {
        this.answersRepository = answersRepository;
        this.questionRepository = questionRepository;
    }

    // 답변 등록
    public Long createAnswer(Long qno, AnswerDTO answerDTO) {
        QuestionsEntity questionEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        AnswersEntity answersEntity = answerDTO.toEntity();
        answersEntity.setQuestions(questionEntity);
        answersEntity.setStatus(QnAStatus.답변대기); // 기본 상태를 "답변 대기"로 설정

        answersRepository.save(answersEntity);
        return answersEntity.getAno();
    }

    // 답변 수정
    public void updateAnswer(Long ano, AnswerDTO answerDTO) {
        AnswersEntity answersEntity = answersRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        answersEntity.setAcontent(answerDTO.getAcontent());
        answersEntity.setStatus(answerDTO.getStatus());

        answersRepository.save(answersEntity);
    }

    // 답변 삭제
    public void deleteAnswer(Long ano) {
        AnswersEntity answersEntity = answersRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        answersRepository.delete(answersEntity);
    }

    // 답변 상태 변경 (전체, 답변 완료, 답변 대기)
    public void updateAnswerStatus(Long ano, QnAStatus status) {
        AnswersEntity answersEntity = answersRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        answersEntity.setStatus(status); // 상태 변경
        answersRepository.save(answersEntity);
    }
}
