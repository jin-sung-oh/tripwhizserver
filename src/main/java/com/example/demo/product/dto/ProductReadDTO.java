package com.example.demo.product.dto;

import com.example.demo.util.file.domain.AttachFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDTO {

    private Long pno;               // 상품 번호
    private String pname;            // 상품 이름
    private String pdesc;            // 상품 설명
    private int price;               // 상품 가격
    private Long cno;        // 상위 카테고리 ID
    private Long scno;    // 하위 카테고리 ID



}
