package com.example.demo.common.service;

import com.example.demo.common.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

}
