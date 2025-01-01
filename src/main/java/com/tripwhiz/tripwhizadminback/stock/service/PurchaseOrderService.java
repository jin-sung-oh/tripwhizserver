package com.tripwhiz.tripwhizadminback.stock.service;

import com.tripwhiz.tripwhizadminback.product.entity.Product;
import com.tripwhiz.tripwhizadminback.stock.entity.PurchaseOrder;
import com.tripwhiz.tripwhizadminback.stock.entity.PurchaseOrderDetail;
import com.tripwhiz.tripwhizadminback.stock.entity.PurchaseOrderStatus;
import com.tripwhiz.tripwhizadminback.stock.dto.PurchaseOrderDTO;
import com.tripwhiz.tripwhizadminback.stock.dto.PurchaseOrderDetailDTO;
import com.tripwhiz.tripwhizadminback.stock.repository.PurchaseOrderDetailRepository;
import com.tripwhiz.tripwhizadminback.stock.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto, List<PurchaseOrderDetailDTO> detailDTOS) {
        // 1. PurchaseOrder 생성 및 저장
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .totalamount(dto.getTotalamount())
                .totalprice(dto.getTotalprice())
                .status(PurchaseOrderStatus.승인대기)
                .build();
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        // 2. PurchaseOrderDetail 생성 및 저장
        List<PurchaseOrderDetail> details = detailDTOS.stream()
                .map(detailDTO -> PurchaseOrderDetail.builder()
                        .amount(detailDTO.getAmount())
                        .purchaseOrder(savedPurchaseOrder) // 저장된 PurchaseOrder와 연결
                        .product(Product.builder().pno(detailDTO.getPno()).build()) // Product 참조
                        .build())
                .collect(Collectors.toList()); // .toList() -> Java 16+ 또는 .collect(Collectors.toList())

        purchaseOrderDetailRepository.saveAll(details);

        // 3. 결과 DTO 반환
        return mapToDTO(savedPurchaseOrder);
    }

    /**
     * Entity -> DTO 변환
     */


    /**
     * 모든 발주 조회
     */
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * 발주 승인
     */
    public void approvePurchaseOrder(Long pono) {
        PurchaseOrder order = purchaseOrderRepository.findById(pono)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        order.setStatus(PurchaseOrderStatus.승인완료);
        purchaseOrderRepository.save(order);
    }

    private PurchaseOrderDTO mapToDTO(PurchaseOrder purchaseOrder) {
        return PurchaseOrderDTO.builder()
                .pono(purchaseOrder.getPono())
                .totalamount(purchaseOrder.getTotalamount())
                .totalprice(purchaseOrder.getTotalprice())
                .status(purchaseOrder.getStatus())
                .createdDate(purchaseOrder.getCreatedDate())
                .build();
    }
}
