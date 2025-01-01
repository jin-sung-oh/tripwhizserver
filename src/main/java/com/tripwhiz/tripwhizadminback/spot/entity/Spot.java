package com.tripwhiz.tripwhizadminback.spot.entity;

import com.tripwhiz.tripwhizadminback.storeowner.entity.StoreOwner;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "spot")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spno; // 지점 번호

    @Column(name = "spotname", nullable = false, length = 50)
    private String spotname; // 지점 이름

    @Column(name = "address", length = 500)
    private String address; // 지점 주소

//    @Column(name = "tel", length = 50)
//    private String tel; // 지점 전화번호 이마트 사이트에도 없는데 굳이 받아올 필요가 없다고 생각해서 주석처리함(JH)

    @Column(name = "url", nullable = false)
    private String url; // 크롤링해서 받아오는 구글맵 경로

    @Column(name = "latitude", nullable = true) // 위도
    private Double latitude;

    @Column(name = "longitude", nullable = true) // 경도
    private Double longitude;

    @Column(name = "del_flag", length = 1)
    private boolean delFlag; // 삭제 여부

    @ManyToOne
    @JoinColumn(name = "sno", nullable = false)
    private StoreOwner storeowner; // 점주


}
