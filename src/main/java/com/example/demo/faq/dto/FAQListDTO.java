package com.example.demo.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQListDTO {

    private Long fno;

    private String question;

    private int viewCnt;

    private boolean delFlag;

}
