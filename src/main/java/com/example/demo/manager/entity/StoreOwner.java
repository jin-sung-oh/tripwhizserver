package com.example.demo.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Entity
@Getter
@Setter
@Table(name = "store_owner")
public class StoreOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sno; // 점주 번호

    private String sname; // 이름

    @Column(name = "id", unique = true, length = 50)
    private String id; // 아이디

    @Column(name = "pw", length = 100)
    private String pw; // 비밀번호

    @Column(name = "email", length = 50)
    private String email; // 이메일

    @Column(name = "del_flag",  length = 1)
    private boolean delFlag; // 삭제 여부
}
