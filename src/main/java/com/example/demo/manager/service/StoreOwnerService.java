package com.example.demo.manager.service;

import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.repository.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreOwnerService {

    private final StoreOwnerRepository storeOwnerRepository;

    // 점주 계정 생성
    public StoreOwner save(StoreOwner storeOwner) {
        return storeOwnerRepository.save(storeOwner);
    }

    // 점주 목록 조회
    public List<StoreOwner> findAll() {
        return storeOwnerRepository.findAll();
    }

    // 점주 정보 조회 (S_no로 찾기)
    public Optional<StoreOwner> findByS_no(String s_no) {
        return storeOwnerRepository.findById(s_no);
    }

    // 점주 계정 삭제
    public void delete(String s_no) {
        storeOwnerRepository.deleteById(s_no);
    }
}