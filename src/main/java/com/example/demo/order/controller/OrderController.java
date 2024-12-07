package com.example.demo.order.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.order.dto.OrderListDTO;
import com.example.demo.order.dto.OrderReadDTO;
import com.example.demo.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService userOrderService;

    // 주문 생성
    @PostMapping("/receive")
    public ResponseEntity<?> receiveOrder(@RequestParam String email,
                                          @RequestParam Long spno,
                                          @RequestBody OrderReadDTO orderReadDTO) {
        try {
            userOrderService.createOrderFromDTO(email, spno, orderReadDTO);
            return ResponseEntity.ok("주문 데이터가 성공적으로 점주 서버에 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("주문 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 내 주문 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getUserOrders(
            @RequestParam @NotBlank(message = "Email cannot be blank") String email,
            @Valid PageRequestDTO pageRequestDTO) {
        PageResponseDTO<OrderListDTO> response = userOrderService.getUserOrders(email, pageRequestDTO);
        return ResponseEntity.ok(response);
    }

    // 특정 주문 상세 조회
    @GetMapping("/details/{ono}")
    public ResponseEntity<?> getOrderDetails(
            @PathVariable Long ono,
            @RequestParam @NotBlank(message = "Email cannot be blank") String email) {
        try {
            OrderReadDTO orderDetails = userOrderService.getOrderDetails(ono, email);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found or unauthorized access.");
        }
    }

    // 주문 취소
    @PutMapping("/cancel/{ono}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Long ono,
            @RequestParam @NotBlank(message = "Email cannot be blank") String email) {
        try {
            userOrderService.cancelOrder(ono, email);
            return ResponseEntity.ok("주문이 성공적으로 취소되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("주문 취소에 실패했습니다: " + e.getMessage());
        }
    }

    // 지점 변경
    @PutMapping("/changespot/{ono}")
    public ResponseEntity<?> changeSpot(
            @PathVariable Long ono,
            @RequestParam Long newSpotId,
            @RequestParam @NotBlank(message = "Email cannot be blank") String email) {
        try {
            userOrderService.changeSpot(ono, newSpotId, email);
            return ResponseEntity.ok("지점이 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("지점 변경에 실패했습니다: " + e.getMessage());
        }
    }

    // 픽업 날짜 변경
    @PutMapping("/changedate/{ono}")
    public ResponseEntity<?> changePickUpDate(
            @PathVariable Long ono,
            @RequestParam @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}",
                    message = "Invalid date format. Use yyyy-MM-ddTHH:mm") String newPickUpDate,
            @RequestParam @NotBlank(message = "Email cannot be blank") String email) {
        try {
            userOrderService.changePickUpDate(ono, LocalDateTime.parse(newPickUpDate), email);
            return ResponseEntity.ok("픽업 날짜가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("픽업 날짜 변경에 실패했습니다: " + e.getMessage());
        }
    }

    // 특정 주문 항목의 QR 코드 가져오기 (사용자 측에서 RestTemplate 사용)
    @GetMapping("/details/qrcode/{odno}")
    public ResponseEntity<?> getOrderDetailsQRCode(@PathVariable Long odno) {
        try {
            byte[] qrImage = userOrderService.getOrderDetailsQRCodeImage(odno);
            return ResponseEntity.ok(qrImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("QR 코드 생성에 실패했습니다: " + e.getMessage());
        }
    }
}
