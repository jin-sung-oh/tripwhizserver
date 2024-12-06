package com.example.demo.order.service;

import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.fcm.dto.FCMRequestDTO;
import com.example.demo.fcm.service.FCMService;
import com.example.demo.order.domain.Order;
import com.example.demo.order.domain.OrderDetails;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.product.domain.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FCMService fcmService;

    public void createOrder(Order order, String storeOwnerToken) {
        orderRepository.save(order);

        // 점주에게 주문 접수 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(storeOwnerToken)
                .title("새로운 주문이 접수되었습니다")
                .body("주문 번호: " + order.getOno())
                .build());
    }

    public void approveOrder(Long ono, String storeOwnerToken, String userToken) {
        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.changeStatus(OrderStatus.APPROVED);
        orderRepository.save(order);

        // 점주 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(storeOwnerToken)
                .title("주문이 승인되었습니다")
                .body("주문 번호: " + ono)
                .build());

        // 유저 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(userToken)
                .title("주문이 승인되었습니다")
                .body("주문 번호: " + ono)
                .build());
    }

    public void markOrderReady(Long ono, String storeOwnerToken) {
        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.changeStatus(OrderStatus.READY_FOR_PICKUP);
        orderRepository.save(order);

        // 점주 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(storeOwnerToken)
                .title("주문이 준비 완료되었습니다")
                .body("주문 번호: " + ono)
                .build());
    }

    public void handleBaggageRequest(Long requestId, String userToken, String storeOwnerToken) {
        // 점주에게 수화물 보관 신청 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(storeOwnerToken)
                .title("새로운 수화물 보관 신청")
                .body("신청 번호: " + requestId)
                .build());

        // 유저에게 승인 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(userToken)
                .title("수화물 보관 신청이 승인되었습니다")
                .body("신청 번호: " + requestId)
                .build());
    }

    public void notifyPickupArrival(String userToken, String storeOwnerToken, Long ono) {
        // 점주 알림
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(storeOwnerToken)
                .title("사용자가 픽업 지점 근처에 도착했습니다")
                .body("주문 번호: " + ono)
                .build());
    }

    // convertCartToOrderDetails 메서드
    private OrderDetails convertCartToOrderDetails(CartListDTO cartListDTO, Order order, Product product) {

        return OrderDetails.builder()
                .order(order)
                .product(product)
                .amount(cartListDTO.getQty())
//                .price(cartListDTO.getQty() * product.getPrice()) // 단가 * 수량
                .build();

    }

}
