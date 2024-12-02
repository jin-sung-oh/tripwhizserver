package com.example.demo.order.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.order.domain.Order;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.dto.OrderListDTO;
import com.example.demo.order.dto.OrderReadDTO;
import com.example.demo.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/storeowner/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> createOrder(@RequestBody Order order, @RequestParam String storeOwnerToken) {
        orderService.createOrder(order, storeOwnerToken);
        return ResponseEntity.ok("Order created and notification sent to store owner.");
    }

    @PutMapping("/approve/{ono}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> approveOrder(@PathVariable Long ono,
                                               @RequestParam String storeOwnerToken,
                                               @RequestParam String userToken) {
        orderService.approveOrder(ono, storeOwnerToken, userToken);
        return ResponseEntity.ok("Order approved and notifications sent.");
    }

    @PutMapping("/ready/{ono}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> markOrderReady(@PathVariable Long ono, @RequestParam String storeOwnerToken) {
        orderService.markOrderReady(ono, storeOwnerToken);
        return ResponseEntity.ok("Order marked as ready and notification sent.");
    }

    @PostMapping("/baggage/request/{requestId}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> handleBaggageRequest(@PathVariable Long requestId,
                                                       @RequestParam String userToken,
                                                       @RequestParam String storeOwnerToken) {
        orderService.handleBaggageRequest(requestId, userToken, storeOwnerToken);
        return ResponseEntity.ok("Baggage request notifications sent.");
    }

    @PostMapping("/pickup/notify")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<String> notifyPickupArrival(@RequestParam String userToken,
                                                      @RequestParam String storeOwnerToken,
                                                      @RequestParam Long ono) {
        orderService.notifyPickupArrival(userToken, storeOwnerToken, ono);
        return ResponseEntity.ok("Pickup arrival notification sent.");
    }
}