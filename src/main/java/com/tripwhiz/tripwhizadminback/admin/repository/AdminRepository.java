package com.tripwhiz.tripwhizadminback.admin.repository;

import com.tripwhiz.tripwhizadminback.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findById(String id);
}
