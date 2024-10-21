package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQEntity, Long> {

    Optional<FAQEntity> findAll(Long fno);

}
