package com.example.demo.admin.repository;

import com.example.demo.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // ID 타입을 String으로 변경
    Optional<Admin> findById(String id);  // Admin 엔티티에서 id가 String 타입이므로 파라미터도 String 타입이어야 합니다.
}
