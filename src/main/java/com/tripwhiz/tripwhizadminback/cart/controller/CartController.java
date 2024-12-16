package com.tripwhiz.tripwhizadminback.cart.controller;

import com.tripwhiz.tripwhizadminback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizadminback.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addToCart(@RequestBody CartListDTO cartListDTO) {
        cartService.addToCart(cartListDTO);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // 장바구니 항목 조회
//    @GetMapping("/items")
//    public ResponseEntity<List<CartProductDTO>> getCartItems() {
//        List<CartProductDTO> cartItems = cartService.getCartItems();
//        return ResponseEntity.ok(cartItems);
//    }

    @PatchMapping("/changeQty")
    public ResponseEntity<Void> changeQty(
            @RequestParam Long pno,
            @RequestParam int qty) {

        cartService.changeQty(pno, qty);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

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

}