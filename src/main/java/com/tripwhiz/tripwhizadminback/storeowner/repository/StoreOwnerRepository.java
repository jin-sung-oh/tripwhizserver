package com.tripwhiz.tripwhizadminback.storeowner.repository;

import com.tripwhiz.tripwhizadminback.storeowner.entity.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreOwnerRepository extends JpaRepository<StoreOwner, Long> {

    // 점주 ID로 조회하는 메서드 추가
    Optional<StoreOwner> findById(String id);
}
