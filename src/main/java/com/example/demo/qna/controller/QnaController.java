package com.example.demo.qna.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.dto.QuestionDTO;
import com.example.demo.qna.service.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/qna")
@Log4j2
@RequiredArgsConstructor

public class QnaController {

    private final QnAService qnaService;

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

    // 새로운 QnA 항목을 추가하는 메서드 (Create)
    @PostMapping("/add")
    public ResponseEntity<Long> createQuestion(@RequestPart("title") String title,
                                               @RequestPart("status") String status,
                                               @RequestPart("writer") String writer,
                                               @RequestPart("qcontent") String qcontent,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        log.info("Creating new QnA with title: {}, status: {}, writer: {}, qcontent: {}", title, status, writer, qcontent);

        try {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setTitle(title);
            questionDTO.setStatus(QnAStatus.valueOf(status)); // Enum으로 처리
            questionDTO.setWriter(writer);
            questionDTO.setQcontent(qcontent);

            Long qno = qnaService.createQuestionWithFiles(questionDTO, files);
            return ResponseEntity.ok(qno);
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // QnA 항목을 수정하는 메서드 (Update)
    @PutMapping("/update/{qno}")
    public ResponseEntity<String> updateQuestion(@PathVariable("qno") Long qno,
                                                 @RequestPart("title") String title,
                                                 @RequestPart("status") String status,
                                                 @RequestPart("writer") String writer,
                                                 @RequestPart("qcontent") String qcontent,
                                                 @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        log.info("Updating QnA with ID: {}", qno);

        try {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setTitle(title);
            questionDTO.setStatus(QnAStatus.valueOf(status)); // Enum으로 처리
            questionDTO.setWriter(writer);
            questionDTO.setQcontent(qcontent);

            qnaService.updateQuestionWithFiles(qno, questionDTO, files);
            return ResponseEntity.ok("Question updated successfully.");
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // QnA 항목을 삭제하는 메서드 (Delete)
    @DeleteMapping("/delete/{qno}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("qno") Long qno) {
        log.info("Deleting QnA with ID: {}", qno);
        qnaService.deleteQuestion(qno);
        return ResponseEntity.ok("Question deleted successfully.");
    }
}
