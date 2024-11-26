package com.example.demo.store.dto.SpotDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotDTO {
    private Long spno; // 지점 번호
    private String spotname; // 지점 이름
    private String address; // 지점 주소
    private String tel; // 지점 전화번호
    private Integer sno; // 점주 번호 (nullable로 설정)
    private String sname; // 점주 이름 (nullable로 설정)
}
