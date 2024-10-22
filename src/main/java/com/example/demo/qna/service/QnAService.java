package com.example.demo.qna.service;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.qna.domain.AnswersEntity;
import com.example.demo.qna.domain.QuestionsEntity;
import com.example.demo.qna.dto.QuestionDTO;
import com.example.demo.qna.repository.AnswersRepository;
import com.example.demo.qna.repository.QuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class QnAService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswersRepository answersRepository;

    // 질문 조회 (작성자는 비공개 질문도 조회 가능)
    public QuestionDTO getQuestion(Long qno) {
        QuestionsEntity questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        return new QuestionDTO(questionsEntity);
    }

    // 새로운 질문 생성 (Create) - 파일 업로드 포함
    public Long createQuestionWithFile(QuestionDTO questionDTO, MultipartFile file) throws IOException {
        QuestionsEntity questionsEntity = questionDTO.toEntity();

        // 파일 처리 로직 추가 (예: 파일이 있는 경우만 처리)
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            byte[] fileContent = file.getBytes();
            // 파일 저장 로직 추가
            log.info("File {} uploaded successfully.", fileName);
        }

        questionRepository.save(questionsEntity);
        return questionsEntity.getQno();
    }

    // 질문 수정 (Update)
    public void updateQuestion(Long qno, QuestionDTO questionDTO) {
        QuestionsEntity questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        questionsEntity.setTitle(questionDTO.getTitle());
        questionsEntity.setQcontent(questionDTO.getQcontent());
        questionsEntity.setStatus(questionDTO.getStatus());

        questionRepository.save(questionsEntity);
    }

    // 질문 삭제 (Delete)
    public void deleteQuestion(Long qno) {
        QuestionsEntity questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));
        questionRepository.delete(questionsEntity);
    }

    // 질문 목록을 페이징 처리하여 반환하는 메서드 (Read - List)
    public PageResponseDTO<QuestionDTO> getList(PageRequestDTO pageRequestDTO) {
        // PageRequestDTO를 Pageable 객체로 변환
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending());

        // 모든 질문을 페이징 처리하여 조회
        Page<QuestionsEntity> result = questionRepository.findAll(pageable);

        // QuestionsEntity를 QuestionDTO로 변환
        List<QuestionDTO> dtoList = result.stream()
                .map(questionsEntity -> new QuestionDTO(questionsEntity))
                .collect(Collectors.toList());

        // PageResponseDTO로 결과 반환
        return PageResponseDTO.<QuestionDTO>withAll()
                .totalCount((int) result.getTotalElements())
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .build();
    }
}
