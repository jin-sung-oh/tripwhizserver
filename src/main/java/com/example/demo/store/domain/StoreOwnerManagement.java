//package com.example.demo.store.domain;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//public class StoreOwnerManagement {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키로 설정된 자동 증가 필드
//    private Long sid; // 관리 ID (자동 증가 필드)
//
//    private String role; // 점주 관리 권한 (예: "Admin", "Manager")
//
//    @ManyToOne
//    @JoinColumn(name = "s_no", nullable = false)
//    private StoreOwner storeowner; // 점주
//}
