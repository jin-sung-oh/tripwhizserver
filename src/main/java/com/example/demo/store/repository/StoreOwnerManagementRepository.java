package com.example.demo.store.repository;

import com.example.demo.store.domain.StoreOwnerManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOwnerManagementRepository extends JpaRepository<StoreOwnerManagement, Long> {
}
