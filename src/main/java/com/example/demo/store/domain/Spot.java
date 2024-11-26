package com.example.demo.store.domain;

import com.example.demo.manager.entity.StoreOwner;
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

    @Column(name = "address", length = 100)
    private String address; // 지점 주소

    @Column(name = "tel", length = 50)
    private String tel; // 지점 전화번호

    @Column(name = "del_flag", length = 1)
    private boolean delFlag; // 삭제 여부

    @ManyToOne
    @JoinColumn(name = "sno", nullable = false)
    private StoreOwner storeowner; // 점주
}
