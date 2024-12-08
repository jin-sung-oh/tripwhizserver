package com.example.demo.manager.dto;

import lombok.Data;

@Data
public class StoreOwnerDTO {

    private String id; // 아이디
    private String pw; // 비밀번호
    private String email; // 이메일
    private String role; // 권한
}