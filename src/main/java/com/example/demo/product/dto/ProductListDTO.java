package com.example.demo.product.dto;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ThemeCategory;
import com.example.demo.util.file.domain.AttachFile;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductListDTO {

    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private Long cno;
    private Long scno;
    private List<AttachFile> attachFiles;
    private Long tno;

    // 엔티티 변환 메소드
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

