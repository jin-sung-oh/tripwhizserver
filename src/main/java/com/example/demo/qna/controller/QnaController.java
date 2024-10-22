package com.example.demo.qna.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.qna.service.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qna")
@Log4j2
@RequiredArgsConstructor
public class QnaController {

    private final QnAService qnaService;

    @GetMapping("/list")
    public PageResponseDTO<Q> list(PageRequestDTO pageRequestDTO) {
        return qnaService.list(pageRequestDTO);
    }


}
