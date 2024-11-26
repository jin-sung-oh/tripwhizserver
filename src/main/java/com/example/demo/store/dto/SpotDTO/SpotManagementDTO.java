package com.example.demo.store.dto.SpotDTO;

import lombok.Builder;
import lombok.Data;

public class SpotManagementDTO {

    @Data
    @Builder
    public static class SpotManagementListDTO {
        private int id; // 관리 번호
        private String address; // 관리 주소
        private String tel; // 관리 전화번호
        private Long spno; // 지점 번호
        private int sno; // 점주 번호
    }
}
