package com.example.demo.store.dto.SpotDTO;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SpotDTO {

    private Long spno;
    private String spotname;
    private String address;
    private String tel;
    private Double latitude; // 위도
    private Double longitude; // 경도
    private Long sno; // 점주 ID
    private String sname; // 점주 이름

    @Builder
    public SpotDTO(Long spno, String spotname, String address, String tel, Double latitude, Double longitude, Long sno, String sname) {
        this.spno = spno;
        this.spotname = spotname;
        this.address = address;
        this.tel = tel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sno = sno;
        this.sname = sname;
    }
}
