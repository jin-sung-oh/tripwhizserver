package com.example.demo.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList; // 현재 페이지 데이터 목록
    private List<Integer> pageNumList; // 현재 표시할 페이지 번호 목록
    private PageRequestDTO pageRequestDTO; // 요청 DTO
    private boolean prev, next; // 이전/다음 버튼 활성화 여부
    private int totalCount; // 총 데이터 개수
    private int prevPage, nextPage; // 이전/다음 페이지 번호
    private int totalPage; // 총 페이지 수
    private int current; // 현재 페이지 번호
    private List<Long> tnos;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) totalCount;

        // 총 페이지 수 계산
        this.totalPage = (int) Math.ceil((double) totalCount / pageRequestDTO.getSize());

        // 현재 페이지 그룹의 끝 페이지 계산
        int end = (int) (Math.ceil((double) pageRequestDTO.getPage() / 10) * 10);

        // 현재 페이지 그룹의 시작 페이지 계산
        int start = end - 9;

        // 끝 페이지가 총 페이지 수를 초과하지 않도록 설정
        end = Math.min(end, totalPage);

        // 이전/다음 버튼 활성화 여부 설정
        this.prev = start > 1;
        this.next = end < totalPage;

        // 이전/다음 페이지 번호 설정
        if (prev) {
            this.prevPage = start - 1;
        }
        if (next) {
            this.nextPage = end + 1;
        }

        // 현재 페이지 번호 목록 생성
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        // 현재 페이지 설정
        this.current = pageRequestDTO.getPage();
    }
}
