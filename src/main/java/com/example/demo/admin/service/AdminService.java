package com.example.demo.admin.service;

import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    public Admin save(Admin admin) {
        // ID 중복 체크 로직 추가
        if (adminRepository.findById(admin.getId()).isPresent()) {
            throw new RuntimeException("ID already exists.");
        }

        // 비밀번호 암호화
        admin.setPw(passwordEncoder.encode(admin.getPw())); // 비밀번호를 암호화하여 저장

        return adminRepository.save(admin);
    }

    public Optional<Admin> findById(String id) {
        return adminRepository.findById(id);
    }

    // 로그인 시 비밀번호를 비교하는 메서드를 여기에 추가할 필요는 없습니다. 로그인은 별도의 컨트롤러에서 처리됩니다.
}
