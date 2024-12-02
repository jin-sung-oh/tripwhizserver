package com.example.demo.stock.repository;

import com.example.demo.stock.domain.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {
}
