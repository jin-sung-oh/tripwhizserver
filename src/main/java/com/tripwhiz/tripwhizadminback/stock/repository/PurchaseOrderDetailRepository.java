package com.tripwhiz.tripwhizadminback.stock.repository;

import com.tripwhiz.tripwhizadminback.stock.entity.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {
}
