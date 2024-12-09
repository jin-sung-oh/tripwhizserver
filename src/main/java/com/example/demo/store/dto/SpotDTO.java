package com.example.demo.store.dto;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SpotDTO {

    private Long spno;
    private String spotname;
    private String address;
//    private String tel; 엔티티에도 tel 주석처리함(JH)
    private String url;
    private Double latitude; // 위도
    private Double longitude; // 경도
    private Long sno; // 점주 ID
    private String sname; // 점주 이름

    
}
