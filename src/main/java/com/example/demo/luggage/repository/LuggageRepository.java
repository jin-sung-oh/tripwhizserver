package com.example.demo.luggage.repository;


import com.example.demo.luggage.entity.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LuggageRepository extends JpaRepository<Luggage, Long> {

    // 이메일을 기준으로 Luggage 목록 조회
    List<Luggage> findByMemberEmail(String email); // member의 email을 기준으로 Luggage를 조회
}
