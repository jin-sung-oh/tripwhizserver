package com.tripwhiz.tripwhizadminback.storeowner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "storeowner")
public class StoreOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno; // 점주 번호

    private String sname; // 이름

    @Column(name = "id", unique = true, length = 50)
    private String id; // 아이디

    @Column(name = "pw", length = 100)
    private String pw; // 비밀번호

    @Column(name = "email", length = 50)
    private String email; // 이메일

    @Column(name = "del_flag",  length = 1)
    private boolean delFlag; // 삭제 여부

    @Column(name = "role", length = 50)
    private String role; // 권한 (ADMIN or MANAGER)
}
