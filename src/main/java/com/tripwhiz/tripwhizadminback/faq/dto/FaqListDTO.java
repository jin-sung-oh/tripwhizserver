package com.tripwhiz.tripwhizadminback.faq.dto;

import com.tripwhiz.tripwhizadminback.faq.entity.FaqCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaqListDTO {

    private Long fno;

    private FaqCategory category;

    private String question;

    private String answer;

    private int viewCnt;

    private boolean delFlag;

}
