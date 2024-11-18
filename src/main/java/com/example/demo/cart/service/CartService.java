package com.example.demo.cart.service;

import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public List<CartListDTO> list() {

        List<CartListDTO> cartItems = cartRepository.findAllCartItems();

        return cartItems.stream()
                .map(cart -> CartListDTO.builder()
                        .bno(cart.getBno())
                        .mno(cart.getMno())
                        .pno(cart.getPno())
                        .totalQty(cart.getTotalQty())
                        .totalPrice(cart.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

    }

}
