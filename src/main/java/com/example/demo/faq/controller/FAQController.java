package com.example.demo.faq.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.faq.dto.FAQListDTO;
import com.example.demo.faq.service.FAQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FAQController {

    @Autowired
    private final FAQService faqService;

    @GetMapping("list")
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        return faqService.list(pageRequestDTO);

    }

}
