package com.example.demo.admin.dto;

import lombok.Data;

@Data
public class AdminDTO {

    private String id; // 아이디
    private String pw; // 비밀번호
    private String role; // 권한
}