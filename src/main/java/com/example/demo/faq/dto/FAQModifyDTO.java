package com.example.demo.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQModifyDTO {

    private Long fno;

    private String category; // CategoryEntity 대신 String으로 변경

    private String question;

    private String answer;
}
