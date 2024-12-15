package com.tripwhiz.tripwhizadminback.stock.controller;

import com.tripwhiz.tripwhizadminback.stock.dto.PurchaseOrderDTO;
import com.tripwhiz.tripwhizadminback.stock.dto.PurchaseOrderDetailDTO;
import com.tripwhiz.tripwhizadminback.stock.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puro")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/add")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(
            @RequestBody PurchaseOrderDTO orderDTO,
            @RequestBody List<PurchaseOrderDetailDTO> detailDTOs) {
        PurchaseOrderDTO createdOrder = purchaseOrderService.createPurchaseOrder(orderDTO, detailDTOs);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("list")
    public ResponseEntity<List<PurchaseOrderDTO>> getAllPurchaseOrders() {
        List<PurchaseOrderDTO> orders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }
    @PutMapping("/{pono}/ok")
    public ResponseEntity<String> approvePurchaseOrder(@PathVariable Long pono) {
        purchaseOrderService.approvePurchaseOrder(pono);
        return ResponseEntity.ok("Purchase order approved");
    }
}
