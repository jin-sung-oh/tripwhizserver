package com.example.demo.faq.dto;

import com.example.demo.faq.domain.FaqCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQReadDTO {

    private Long fno;

    private FaqCategory category;

    private String question;

    private String answer;

}