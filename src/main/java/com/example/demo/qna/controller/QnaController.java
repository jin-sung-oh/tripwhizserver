package com.example.demo.qna.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.qna.dto.QuestionDTO;
import com.example.demo.qna.service.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qna") // QnA 관련 API 요청 경로
@Log4j2
@RequiredArgsConstructor // final 필드를 생성자 주입 방식으로 처리하기 위한 어노테이션
public class QnaController {

    private final QnAService qnaService; // QnA 서비스 클래스 주입

    // QnA 목록을 페이징 처리하여 반환하는 메서드
    @GetMapping("/list")
    public PageResponseDTO<QuestionDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching list of QnA with page request: " + pageRequestDTO);
        return qnaService.getList(pageRequestDTO);
    }

    // 제목에 특정 키워드가 포함된 질문을 검색하는 메서드
    @GetMapping("/search/title")
    public PageResponseDTO<QuestionDTO> searchByTitle(@RequestParam("keyword") String keyword, PageRequestDTO pageRequestDTO) {
        log.info("Searching questions by title with keyword: " + keyword);
        return qnaService.searchByTitle(keyword, pageRequestDTO);
    }

    // 내용에 특정 키워드가 포함된 질문을 검색하는 메서드
    @GetMapping("/search/content")
    public PageResponseDTO<QuestionDTO> searchByContent(@RequestParam("keyword") String keyword, PageRequestDTO pageRequestDTO) {
        log.info("Searching questions by content with keyword: " + keyword);
        return qnaService.searchByContent(keyword, pageRequestDTO);
    }
}
