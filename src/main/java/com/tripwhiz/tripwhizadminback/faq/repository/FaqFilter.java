package com.tripwhiz.tripwhizadminback.faq.repository;

import com.tripwhiz.tripwhizadminback.faq.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqFilter {

    Page<Faq> list(Pageable pageable);

//    Page<FAQEntity> listByCategory( Pageable pageable);
}
