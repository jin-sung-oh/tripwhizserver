package com.tripwhiz.tripwhizadminback.product.entity;

import com.tripwhiz.tripwhizadminback.category.entity.Category;
import com.tripwhiz.tripwhizadminback.category.entity.SubCategory;
import com.tripwhiz.tripwhizadminback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizadminback.util.file.entity.AttachFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"category", "subCategory"})
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Column(nullable = false, length = 50)
    private String pname;

    @Column(length = 1000)
    private String pdesc;

    private int price;

    private boolean delFlag;




    // JH
    @ElementCollection
    @CollectionTable(
            name = "product_images", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "pno") // 외래 키 이름 지정
    )
    @Builder.Default
    private List<AttachFile> attachFiles = new ArrayList<>();

    // 상위 카테고리와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cno") // 외래 키 이름을 지정 (상위 카테고리 ID와 연결)
    private Category category;

    // 하위 카테고리와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_scno") // 외래 키 이름을 지정 (하위 카테고리 ID와 연결)
    private SubCategory subCategory;


    // 삭제 상태 변경 메서드
    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }


    // 상위 카테고리 설정 메서드
    public void setCategory(Category category) {
        this.category = category;
    }

    // 하위 카테고리 설정 메서드
    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public void updateFromDTO(ProductListDTO dto, Category category, SubCategory subCategory) {
        this.pname = dto.getPname();
        this.price = dto.getPrice();
        this.pdesc = dto.getPdesc();
        this.category = category;
        this.subCategory = subCategory;
    }

    // JH
    public void addAttachFile(AttachFile attachFile) {
        if (this.attachFiles == null) {
            this.attachFiles = new ArrayList<>();
        }
        this.attachFiles.add(attachFile);
    }


}
