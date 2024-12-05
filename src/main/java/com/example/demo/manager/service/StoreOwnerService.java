package com.example.demo.manager.service;

import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.repository.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreOwnerService {

    private final StoreOwnerRepository storeOwnerRepository; // Repository 주입
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    // 점주 저장
    public StoreOwner save(StoreOwner storeOwner) {
        // ID 중복 체크
        if (storeOwnerRepository.findById(storeOwner.getId()).isPresent()) {
            throw new RuntimeException("ID already exists.");
        }

        // 비밀번호 암호화
        storeOwner.setPw(passwordEncoder.encode(storeOwner.getPw()));
        return storeOwnerRepository.save(storeOwner);
    }

    // 점주 목록 조회
    public List<StoreOwner> findAll() {
        return storeOwnerRepository.findAll();
    }

    // 점주 삭제
    public boolean delete(Long sno) { // sNo로 삭제
        if (storeOwnerRepository.existsById(sno)) {
            storeOwnerRepository.deleteById(sno);
            return true; // 삭제 성공
        }
        return false; // 삭제 실패: ID 없음
    }

    // 점주 ID로 조회
    public Optional<StoreOwner> findById(String id) {
        return storeOwnerRepository.findById(id);
    }
}
