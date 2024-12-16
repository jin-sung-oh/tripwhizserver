package com.tripwhiz.tripwhizadminback.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    @Min(value = 1, message = "over 1")
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "set over 10")
    @Max(value = 100, message = "cannot over 100")
    private int size = 10;

    // 상위 카테고리 ID (예: cno)
    private Long categoryCno;

    // 하위 카테고리 ID (예: scno)
    private Long subCategoryScno;

    private List<Long> tnos; // 테마 카테고리 리스트

    // PageRequestDTO를 Pageable 객체로 변환
    public Pageable toPageable() {
        return PageRequest.of(page - 1, size); // page는 0부터 시작하기 때문에 -1을 해줍니다.
    }


}

