package com.example.demo.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev, next; // 이전 및 다음 버튼 상태
    private int totalCount, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount, int totalPage, boolean prev, boolean next) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) totalCount;
        this.totalPage = totalPage;
        this.current = pageRequestDTO.getPage();
        this.prev = prev; // 이전 버튼 여부
        this.next = next; // 다음 버튼 여부
    }
}
