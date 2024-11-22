package com.example.demo.manager.repository;

import com.example.demo.manager.entity.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOwnerRepository extends JpaRepository<StoreOwner, Integer> {
    // 기본적인 CRUD 메서드는 JpaRepository에서 제공되므로 추가할 필요 없습니다.
    // 예시: findByS_no, findAll, deleteByS_no 등
}