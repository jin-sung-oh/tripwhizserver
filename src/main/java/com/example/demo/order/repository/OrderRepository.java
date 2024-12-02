package com.example.demo.order.repository;

import com.example.demo.order.domain.Order;
import com.example.demo.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByMemberEmail(String memberEmail, Pageable pageable); // 사용자 이메일로 주문 검색

    List<Order> findByStatus(OrderStatus status); // 특정 상태의 주문 검색
}