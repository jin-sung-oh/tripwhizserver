package com.tripwhiz.tripwhizadminback.qna.controller;

import com.tripwhiz.tripwhizadminback.qna.domain.QnAStatus;
import com.tripwhiz.tripwhizadminback.qna.dto.AnswerDTO;
import com.tripwhiz.tripwhizadminback.qna.service.AnswersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ans")
@RequiredArgsConstructor
public class AnswersController {

    private final AnswersService answersService;

    // 답변 등록
    @PostMapping("/add/{qno}")
    public ResponseEntity<Long> createAnswer(@PathVariable Long qno, @RequestBody AnswerDTO answerDTO) {
//        if (answerDTO.getWriter() == null || answerDTO.getWriter().isEmpty()) {
//            throw new IllegalArgumentException("작성자는 필수 입력 항목입니다.");
//        }

        Long answerId = answersService.createAnswer(qno, answerDTO);
        return ResponseEntity.ok(answerId);
    }

    // 답변 수정
    @PutMapping("/update/{ano}")
    public ResponseEntity<Void> updateAnswer(@PathVariable Long ano, @RequestBody AnswerDTO answerDTO) {
        answersService.updateAnswer(ano, answerDTO);
        return ResponseEntity.ok().build();
    }

    // 답변 삭제
    @DeleteMapping("/delete/{ano}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long ano) {
        answersService.deleteAnswer(ano);
        return ResponseEntity.ok().build();
    }

    // 답변 상태 변경
    @PutMapping("/status/{ano}")
    public ResponseEntity<Void> updateAnswerStatus(@PathVariable Long ano, @RequestBody QnAStatus status) {
        answersService.updateAnswerStatus(ano, status);
        return ResponseEntity.ok().build();
    }
}
