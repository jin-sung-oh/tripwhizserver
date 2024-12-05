package com.example.demo.cart.controller;

import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.cart.dto.CartProductDTO;
import com.example.demo.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Log4j2
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 멤버별 장바구니 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<CartListDTO>> list(@RequestHeader("email") String email) {

        List<CartListDTO> cartItems = cartService.list(email);

        log.info(cartItems);

        return ResponseEntity.ok(cartItems);

    }

    // 장바구니에 물건 추가
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartProductDTO cartProductDTO) {
        cartService.addToCart(cartProductDTO);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // 장바구니 항목 조회
//    @GetMapping("/items")
//    public ResponseEntity<List<CartProductDTO>> getCartItems() {
//        List<CartProductDTO> cartItems = cartService.getCartItems();
//        return ResponseEntity.ok(cartItems);
//    }

    @DeleteMapping("delete/{pno}")
    public ResponseEntity<Void> deleteByProduct(
            @RequestHeader("email") String email,
            @PathVariable("pno") Long pno) {

        cartService.softDeleteByProduct(email, pno); // Service 호출
        log.info("Product deleted successfully");
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    @DeleteMapping("delete/all")
    public ResponseEntity<Void> deleteAll(
            @RequestHeader("email") String email) {

        cartService.softDeleteAll(email); // Service 호출
        log.info("Product deleted successfully");
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ResponseEntity<String> createOrder(@RequestHeader String email) {

        // 주문 생성 서비스 호출
        log.info("Received order creation request for email: {}", email);

        cartService.saveCart(email);

        log.info("Order successfully created and sent to Admin API for email: {}", email);

        // 성공 응답 반환
        return ResponseEntity.ok("Order successfully created and sent to Admin API.");
    }

}