package com.tripwhiz.tripwhizadminback.order.repository;

import com.tripwhiz.tripwhizadminback.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByMemberEmail(String memberEmail, Pageable pageable); // Member 이메일로 검색
}
