package com.tripwhiz.tripwhizadminback.product.dto;

import com.tripwhiz.tripwhizadminback.category.entity.Category;
import com.tripwhiz.tripwhizadminback.category.entity.SubCategory;
import com.tripwhiz.tripwhizadminback.util.file.entity.AttachFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductReadDTO {

    private Long pno;               // 상품 번호
    private String pname;           // 상품 이름
    private String pdesc;           // 상품 설명
    private int price;              // 상품 가격
    private Long cno;               // 상위 카테고리 ID
    private Long scno;              // 하위 카테고리 ID
    private List<AttachFile> attachFiles;

    // Category 객체로 매핑
    private Category category;  // 카테고리 객체
    private SubCategory subCategory;  // 서브 카테고리 객체

    // 명시적인 생성자 추가
    public ProductReadDTO(Long pno, String pname, String pdesc, int price, Long cno, Long scno, List<AttachFile> attachFiles) {
        this.pno = pno;
        this.pname = pname;
        this.pdesc = pdesc;
        this.price = price;
        this.cno = cno;
        this.scno = scno;
        this.attachFiles = attachFiles;
    }
}
