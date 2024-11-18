package com.example.demo.cart.controller;

import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Log4j2
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/list")
    public ResponseEntity<List<CartListDTO>> cartList() {

        List<CartListDTO> cartItems = cartService.list();

        log.info(cartItems);

        return ResponseEntity.ok(cartItems);

    }

}