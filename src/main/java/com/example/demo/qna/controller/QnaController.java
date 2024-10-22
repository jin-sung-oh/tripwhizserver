package com.example.demo.qna.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.qna.dto.QuestionDTO;
import com.example.demo.qna.service.QnAService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/qna") // QnA 관련 API 요청 경로
@Log4j2
@RequiredArgsConstructor // final 필드를 생성자 주입 방식으로 처리하기 위한 어노테이션
public class QnaController {

    private final QnAService qnaService; // QnA 서비스 클래스 주입

    // QnA 목록을 페이징 처리하여 반환하는 메서드 (Read - List)
    @GetMapping("/list")
    public PageResponseDTO<QuestionDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching list of QnA with page request: {}", pageRequestDTO);
        return qnaService.getList(pageRequestDTO);
    }

    // 특정 QnA 항목을 조회하는 메서드 (Read - Get)
    @GetMapping("/{qno}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("qno") Long qno) {
        log.info("Fetching QnA with ID: {}", qno);
        QuestionDTO questionDTO = qnaService.getQuestion(qno);
        return ResponseEntity.ok(questionDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<Long> createQuestion(@RequestPart("question") String questionData,
                                               @RequestPart(value = "file", required = false) MultipartFile file) {
        log.info("Creating new QnA: {}", questionData);

        try {
            // JSON을 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            QuestionDTO questionDTO = objectMapper.readValue(questionData, QuestionDTO.class);

            Long qno = qnaService.createQuestionWithFile(questionDTO, file);
            return ResponseEntity.ok(qno);
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // QnA 항목을 수정하는 메서드 (Update)
    @PutMapping("/update/{qno}")
    public ResponseEntity<String> updateQuestion(@PathVariable("qno") Long qno, @RequestBody QuestionDTO questionDTO) {
        log.info("Updating QnA with ID: {}", qno);
        qnaService.updateQuestion(qno, questionDTO);
        return ResponseEntity.ok("Question updated successfully.");
    }

    // QnA 항목을 삭제하는 메서드 (Delete)
    @DeleteMapping("/delete/{qno}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("qno") Long qno) {
        log.info("Deleting QnA with ID: {}", qno);
        qnaService.deleteQuestion(qno);
        return ResponseEntity.ok("Question deleted successfully.");
    }
}
