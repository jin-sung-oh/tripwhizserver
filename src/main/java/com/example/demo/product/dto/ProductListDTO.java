package com.example.demo.product.dto;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ThemeCategory;
import com.example.demo.util.file.domain.AttachFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.product.domain.Product;
import com.example.demo.util.file.domain.AttachFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드는 무시
public class ProductListDTO {

    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private Long cno; // 카테고리 ID
    private Long scno; // 서브 카테고리 ID
    @Builder.Default
    private List<Long> tnos = new ArrayList<>();
    private List<AttachFile> attachFiles; // 첨부 파일

//    // JSON 필드에서 category 객체를 cno로 매핑
//    @JsonProperty("category")
//    public void setCategoryFromJson(Object category) {
//        if (category instanceof java.util.Map) {
//            Object cnoValue = ((java.util.Map<?, ?>) category).get("cno");
//            if (cnoValue instanceof Number) {
//                this.cno = ((Number) cnoValue).longValue();
//            }
//        }
//    }
//
//    // JSON 필드에서 subCategory 객체를 scno로 매핑
//    @JsonProperty("subCategory")
//    public void setSubCategoryFromJson(Object subCategory) {
//        if (subCategory instanceof java.util.Map) {
//            Object scnoValue = ((java.util.Map<?, ?>) subCategory).get("scno");
//            if (scnoValue instanceof Number) {
//                this.scno = ((Number) scnoValue).longValue();
//            }
//        }
//    }

    // 엔티티 변환 메서드
    public Product toEntity(Category category, SubCategory subCategory) {
        return Product.builder()
                .pname(this.pname)
                .price(this.price)
                .pdesc(this.pdesc)
                .category(category)
                .subCategory(subCategory)
                .attachFiles(this.attachFiles)
                .build();
    }
}


//    public ProductListDTO(Long pno, String pdesc, String pname, int price, Long cno, Long scno) {
//        this.pno = pno;
//        this.pdesc = pdesc;
//        this.pname = pname;
//        this.price = price;
//        this.cno = cno;
//        this.scno = scno;
//

