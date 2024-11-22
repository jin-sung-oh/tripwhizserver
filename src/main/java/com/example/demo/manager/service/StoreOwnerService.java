package com.example.demo.manager.service;

import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.repository.StoreOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreOwnerService {
    @Autowired
    private StoreOwnerRepository repository;

    // 점주 목록 조회
    public List<StoreOwner> findAll() {
        return repository.findAll();
    }

    // 점주 계정 생성
    public StoreOwner save(StoreOwner storeOwner) {
        // 추가 로직이 필요하면 여기에 작성
        return repository.save(storeOwner); // StoreOwner 객체 저장
    }

    public boolean delete(int sno) { // sNo로 수정
        if (repository.existsById(sno)) {
            repository.deleteById(sno);
            return true; // 삭제 성공
        }
        return false; // 삭제 실패: ID 없음
    }
}
