package com.example.demo.order.repository;

import com.example.demo.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByMemberEmail(String memberEmail, Pageable pageable); // Member 이메일로 검색
}
