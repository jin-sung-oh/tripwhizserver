package com.tripwhiz.tripwhizadminback.qna.service;

import com.tripwhiz.tripwhizadminback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizadminback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import com.tripwhiz.tripwhizadminback.qna.dto.QuestionDTO;
import com.tripwhiz.tripwhizadminback.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

    // 새로운 질문 생성 (Create) - 여러 파일 업로드 포함
    public Long createQuestionWithFiles(QuestionDTO questionDTO, List<MultipartFile> files) throws IOException {
        Questions questionsEntity = questionDTO.toEntity();

        // 파일이 존재하는 경우 파일 처리 로직 추가
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    byte[] fileContent = file.getBytes();
                    log.info("File {} uploaded successfully.", fileName);

                    // 파일 저장 로직을 구현할 수 있습니다 (예: 데이터베이스 또는 파일 시스템에 저장)
                    // 여기서는 간단하게 로그만 출력합니다.
                }
            }
        }

        questionRepository.save(questionsEntity);
        return questionsEntity.getQno();
    }

    // 질문 수정 (Update) - 파일 처리 포함
    public void updateQuestionWithFiles(Long qno, QuestionDTO questionDTO, List<MultipartFile> files) throws IOException {
        Questions questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        questionsEntity.setTitle(questionDTO.getTitle());
        questionsEntity.setQcontent(questionDTO.getQcontent());
        questionsEntity.setStatus(questionDTO.getStatus());

        // 수정 시 새로운 파일을 추가할 수 있게 파일 처리 로직 추가
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    byte[] fileContent = file.getBytes();
                    log.info("File {} uploaded successfully.", fileName);

                    // 파일 저장 로직을 구현할 수 있습니다 (예: 데이터베이스 또는 파일 시스템에 저장)
                }
            }
        }

        questionRepository.save(questionsEntity);
    }

    // 질문 조회 (Read)
    public QuestionDTO getQuestion(Long qno) {
        Questions questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));

        return new QuestionDTO(questionsEntity);
    }

    // 질문 삭제 (Delete)
    public void deleteQuestion(Long qno) {
        Questions questionsEntity = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다"));
        questionRepository.delete(questionsEntity);
    }

    // 질문 목록을 페이징 처리하여 반환하는 메서드 (Read - List)
    public PageResponseDTO<QuestionDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending());

        Page<Questions> result = questionRepository.findAll(pageable);

        List<QuestionDTO> dtoList = result.stream()
                .map(questionsEntity -> new QuestionDTO(questionsEntity))
                .collect(Collectors.toList());

        return PageResponseDTO.<QuestionDTO>withAll()
                .totalCount((int) result.getTotalElements())
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .build();
    }
}
