package com.example.demo.store.domain;

import com.example.demo.manager.entity.StoreOwner;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SpotManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키로 설정된 자동 증가 필드
    private Long spid; // 관리 ID

    private String address; // 관리 지점 주소
    private String tel; // 관리 지점 전화번호

    @ManyToOne
    @JoinColumn(name = "spno", nullable = false)
    private Spot spot;


    @ManyToOne
    @JoinColumn(name = "sno", nullable = false)
    private StoreOwner storeowner; // 점주
}
