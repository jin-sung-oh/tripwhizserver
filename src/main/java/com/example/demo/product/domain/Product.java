package com.example.demo.product.domain;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.domain.ThemeCategory;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.util.file.domain.AttachFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Column(nullable = false, length = 50)
    private String pname;

    @Lob
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
    @JoinColumn(name = "cno") // 외래 키 이름을 지정 (상위 카테고리 ID와 연결)
    private Category category;

    // 하위 카테고리와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scno") // 외래 키 이름을 지정 (하위 카테고리 ID와 연결)
    private SubCategory subCategory;

    // 테마 카테고리 설정 (예: 휴양, 힐링 등)
    @Enumerated(EnumType.STRING) // Enum 값을 데이터베이스에 문자열로 저장
    private ThemeCategory themeCategory;

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

    // 테마 카테고리 설정 메서드
    public void setThemeCategory(ThemeCategory themeCategory) {
        this.themeCategory = themeCategory;
    }

    // DTO의 값으로 엔티티 필드를 업데이트하는 메서드
    public void updateFromDTO(ProductListDTO productListDTO) {
        this.pname = productListDTO.getPname();
        this.price = productListDTO.getPrice();
        this.themeCategory = productListDTO.getThemeCategory();
        // 필요한 필드들을 업데이트
    }

    // JH
    public void addAttachFile(AttachFile attachFile) {
        if (this.attachFiles == null) {
            this.attachFiles = new ArrayList<>();
        }
        this.attachFiles.add(attachFile);
    }


}