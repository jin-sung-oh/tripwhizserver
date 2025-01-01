package com.tripwhiz.tripwhizadminback.qna.service;

import com.tripwhiz.tripwhizadminback.qna.domain.Answers;
import com.tripwhiz.tripwhizadminback.qna.domain.QnAStatus;
import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import com.tripwhiz.tripwhizadminback.qna.dto.AnswerDTO;
import com.tripwhiz.tripwhizadminback.qna.repository.AnswersRepository;
import com.tripwhiz.tripwhizadminback.qna.repository.QuestionRepository;
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
        Questions questionEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        Answers answersEntity = answerDTO.toEntity();
        answersEntity.setQuestions(questionEntity);  // 질문 설정
        answersEntity.setStatus(answerDTO.getStatus());  // 상태 설정
        answersEntity.setWriter(answerDTO.getWriter());  // 작성자 설정

        answersRepository.save(answersEntity);
        return answersEntity.getAno();
    }

    // 답변 수정
    public void updateAnswer(Long ano, AnswerDTO answerDTO) {
        Answers answersEntity = answersRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        answersEntity.setAcontent(answerDTO.getAcontent());
        answersEntity.setStatus(answerDTO.getStatus());
        answersEntity.setWriter(answerDTO.getWriter());

        answersRepository.save(answersEntity);
    }

    // 답변 삭제
    public void deleteAnswer(Long ano) {
        Answers answersEntity = answersRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));
        answersRepository.delete(answersEntity);
    }

    // 답변 상태 변경
    public void updateAnswerStatus(Long ano, QnAStatus status) {
        Answers answersEntity = answersRepository.findById(ano)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        answersEntity.setStatus(status);  // 상태 변경
        answersRepository.save(answersEntity);
    }
}
