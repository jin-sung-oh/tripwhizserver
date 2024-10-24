package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FAQFilter {

    Page<FAQEntity> list(Pageable pageable);

//    Page<FAQEntity> listByCategory( Pageable pageable);
}
