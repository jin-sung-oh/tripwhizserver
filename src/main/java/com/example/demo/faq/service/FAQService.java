package com.example.demo.faq.service;

import com.example.demo.faq.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FAQService {

    private final FAQRepository faqRepository;



}
