package com.example.demo.category.dto;


import com.example.demo.category.domain.SubCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubCategoryDTO {

    private Long scno;

    @NotNull
    private String sname;

    @NotNull
    private Long cno;

    // SubCategory 엔티티에서 SubCategoryDTO로 변환하는 정적 메서드
    public static SubCategoryDTO fromEntity(final SubCategory subCategory) {
        SubCategoryDTO subDTO = new SubCategoryDTO();
        subDTO.setScno(subCategory.getScno()); // SubCategory 엔티티의 ID를 설정
        subDTO.setSname(subCategory.getSname()); // SubCategory 엔티티의 이름을 설정
        subDTO.setCno(subCategory.getCategory().getCno()); // SubCategory 엔티티의 Category의 cno를 설정
        return subDTO;
    }
}
