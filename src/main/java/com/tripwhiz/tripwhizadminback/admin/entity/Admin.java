package com.tripwhiz.tripwhizadminback.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ano")
    private int ano; // 관리자 번호

    @Column(name = "aname", length = 50)
    private String aname; // 이름

    @Column(name = "id",  unique = true, length = 50)
    private String id; // 아이디

    @Column(name = "pw",  length = 100)
    private String pw; // 비밀번호

    @Column(name = "role", length = 50)
    private String role; // 권한 (ADMIN or MANAGER)
}
