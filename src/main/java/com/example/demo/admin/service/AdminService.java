package com.example.demo.admin.service;

import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // 어드민 저장
    public Admin save(Admin admin) {
        if (adminRepository.findById(admin.getId()).isPresent()) {
            throw new RuntimeException("ID already exists.");
        }

        admin.setPw(passwordEncoder.encode(admin.getPw())); // 비밀번호 암호화
        return adminRepository.save(admin);
    }

    // ID로 어드민 찾기
    public Optional<Admin> findById(String id) {
        return adminRepository.findById(id);
    }
}
