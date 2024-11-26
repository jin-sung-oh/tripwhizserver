package com.example.demo.order.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.order.dto.OrderListDTO;
import com.example.demo.order.dto.OrderReadDTO;
import com.example.demo.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ord")
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 리스트 조회
    @GetMapping("/list")
    public PageResponseDTO<OrderListDTO> getOrderList(PageRequestDTO pageRequestDTO) {
        return orderService.getOrderList(pageRequestDTO);
    }

    // 주문 취소
    @PutMapping("/cancel/{ono}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long ono) {
        boolean result = orderService.cancelOrder(ono);
        if (result) {
            return ResponseEntity.ok("Order cancelled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel order. Order may not exist or is already cancelled.");
        }
    }

    // 여러 주문 삭제
    @DeleteMapping("/delete")
    public void deleteOrders(@RequestBody List<Long> orderIds) {
        orderService.deleteOrders(orderIds);
    }

    // 주문 상세 조회
    @GetMapping("/details/{ono}")
    public ResponseEntity<OrderReadDTO> getOrderDetails(@PathVariable Long ono) {
        OrderReadDTO orderDetails = orderService.getOrderDetails(ono);

        log.info(orderDetails);

        return ResponseEntity.ok(orderDetails);
    }

}
