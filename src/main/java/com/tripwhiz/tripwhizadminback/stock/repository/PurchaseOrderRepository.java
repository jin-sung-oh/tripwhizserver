package com.tripwhiz.tripwhizadminback.stock.repository;

import com.tripwhiz.tripwhizadminback.stock.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
