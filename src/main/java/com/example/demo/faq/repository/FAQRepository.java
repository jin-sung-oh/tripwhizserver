package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQEntity, Long> {

    

}
