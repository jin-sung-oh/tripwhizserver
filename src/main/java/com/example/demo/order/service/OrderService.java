package com.example.demo.order.service;

import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.fcm.dto.FCMRequestDTO;
import com.example.demo.fcm.service.FCMService;
import com.example.demo.member.domain.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.order.domain.Order;
import com.example.demo.order.domain.OrderDetails;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.dto.OrderListDTO;
import com.example.demo.order.dto.OrderProductDTO;
import com.example.demo.order.dto.OrderReadDTO;
import com.example.demo.order.repository.OrderDetailsRepository;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.store.domain.Spot;
import com.example.demo.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final SpotRepository spotRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final FCMService fcmService;

    public void createOrderFromDTO(String email, Long spno, OrderReadDTO orderReadDTO) {
        // 1. Order 생성
        Order order = Order.builder()
                .ono(orderReadDTO.getOno())
                .totalPrice(orderReadDTO.getTotalPrice())
                .status(OrderStatus.valueOf(orderReadDTO.getStatus()))
                .pickupdate(orderReadDTO.getPickUpDate())
                .spot(Spot.builder().spno(spno).build()) // Spot은 점주 서버에서는 필요하지 않다면 null 처리
                .member(Member.builder().email(email).build()) // Member 정보도 점주 서버에서는 제외 가능
                .build();
        order = orderRepository.save(order);

        // 2. OrderDetails 생성
        for (OrderProductDTO productDTO : orderReadDTO.getProducts()) {
            OrderDetails orderDetails = OrderDetails.builder()
                    .order(order)
                    .pno(productDTO.getPno())
                    .price(productDTO.getPrice())
                    .amount(productDTO.getAmount())
                    .build();
            orderDetailsRepository.save(orderDetails);
        }
    }

    // 내 주문 리스트 조회
    public PageResponseDTO<OrderListDTO> getUserOrders(String memberEmail, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        Page<Order> result = orderRepository.findByMemberEmail(memberEmail, pageable);

        List<OrderListDTO> dtoList = result.getContent().stream()
                .map(order -> OrderListDTO.builder()
                        .ono(order.getOno())
                        .email(order.getMember().getEmail())
                        .spno(order.getSpot().getSpno())
                        .totalAmount(order.getTotalAmount())
                        .totalPrice(order.getTotalPrice())
                        .createTime(order.getCreatetime())
                        .pickUpDate(order.getPickupdate())
                        .status(order.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return PageResponseDTO.<OrderListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();
    }

    // 특정 주문 상세 조회
    public OrderReadDTO getOrderDetails(Long ono, String memberEmail) {

        // 주문 조회
        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 이메일 검증
        if (!order.getMember().getEmail().equals(memberEmail)) {
            throw new RuntimeException("Unauthorized access to order details.");
        }

        // OrderDetails 조회
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrderOno(ono);

        // OrderDetails -> OrderProductDTO 변환
        List<OrderProductDTO> products = orderDetailsList.stream()
                .map(details -> OrderProductDTO.builder()
                        .pno(details.getPno())  // Product 번호
                        .amount(details.getAmount())   // 수량
                        .price(details.getPrice())     // 가격
                        .build())
                .collect(Collectors.toList());

        // Order -> OrderReadDTO 변환
        return OrderReadDTO.builder()
                .ono(order.getOno())
                .products(products)
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .pickUpDate(order.getPickupdate())
                .spno(order.getSpot().getSpno())
                .build();

    }

    // 주문 취소
    public void cancelOrder(Long ono, String memberEmail) {
        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getEmail().equals(memberEmail)) {
            throw new RuntimeException("Unauthorized access to order.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled.");
        }

        order.changeStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // 유저에게 FCM 알림 전송
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(order.getUserFcmToken())
                .title("주문 취소 알림")
                .body("주문 번호 " + ono + "이(가) 취소되었습니다.")
                .build());

        // 점주에게 FCM 알림 전송
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token("store_owner_fcm_token")
                .title("주문 취소 알림")
                .body("주문 번호 " + ono + "이(가) 취소되었습니다.")
                .build());
    }

    // 지점 변경
    public void changeSpot(Long ono, Long newSpotId, String memberEmail) {
        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getEmail().equals(memberEmail)) {
            throw new RuntimeException("Unauthorized access to order.");
        }

        Spot newSpot = spotRepository.findById(newSpotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        order.setSpot(newSpot);
        orderRepository.save(order);

        // 점주에게 FCM 알림 전송
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token("store_owner_fcm_token")
                .title("지점 변경 알림")
                .body("주문 번호 " + ono + "의 지점이 변경되었습니다.")
                .build());
    }

    // 픽업 날짜 변경
    public void changePickUpDate(Long ono, LocalDateTime newPickUpDate, String memberEmail) {
        Order order = orderRepository.findById(ono)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getEmail().equals(memberEmail)) {
            throw new RuntimeException("Unauthorized access to order.");
        }

        order.setPickupdate(newPickUpDate);
        orderRepository.save(order);

        // 유저에게 FCM 알림 전송
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(order.getUserFcmToken())
                .title("픽업 날짜 변경 알림")
                .body("주문 번호 " + ono + "의 픽업 날짜가 변경되었습니다.")
                .build());

        // 점주에게 FCM 알림 전송
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token("store_owner_fcm_token")
                .title("픽업 날짜 변경 알림")
                .body("주문 번호 " + ono + "의 픽업 날짜가 변경되었습니다.")
                .build());
    }

    // 특정 주문 항목의 QR 코드 이미지 가져오기
    public byte[] getOrderDetailsQRCodeImage(Long odno) {
        OrderDetails orderDetails = orderDetailsRepository.findById(odno)
                .orElseThrow(() -> new RuntimeException("Order details not found"));

        String qrCodeFileName = orderDetails.getQrCodePath(); // 파일명만 저장됨
        if (qrCodeFileName == null) {
            throw new RuntimeException("QR code not available for this order detail.");
        }

        String qrCodeUrl = "https://example.com/qrcode/" + qrCodeFileName; // 파일명으로 URL 구성
        return restTemplate.getForObject(qrCodeUrl, byte[].class);
    }

}
