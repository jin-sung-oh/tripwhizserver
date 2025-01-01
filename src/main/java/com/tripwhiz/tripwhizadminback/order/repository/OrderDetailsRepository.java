package com.tripwhiz.tripwhizadminback.order.repository;

import com.tripwhiz.tripwhizadminback.order.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByOrderOno(Long ono);
}

