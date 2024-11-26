package com.example.demo.store.domain;

import com.example.demo.manager.entity.StoreOwner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "spotmanagement")
public class SpotManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 관리 번호

    @Column(name = "address", length = 100)
    private String address; // 관리 주소

    @Column(name = "tel", length = 50)
    private String tel; // 관리 전화번호

    @ManyToOne
    @JoinColumn(name = "spno", nullable = false)
    private Spot spot; // 지점 번호

    @ManyToOne
    @JoinColumn(name = "sno", nullable = false)
    private StoreOwner storeowner; // 점주 번호
}
