package com.example.demo.cart.service;

import com.example.demo.cart.domain.Cart;
import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.member.domain.Member;
import com.example.demo.product.domain.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    // 장바구니에 물건 추가
    public void addToCart(CartListDTO cartListDTO) {

        Product product = Product.builder().pno(cartListDTO.getPno()).build();
        Member member = Member.builder().email(cartListDTO.getEmail()).build();

        // 장바구니에서 해당 제품 찾기
        Optional<Cart> existingCart = cartRepository.findByProduct(cartListDTO.getPno());

        if (existingCart.isPresent()) {
            // 기존 제품이 있으면 수량 업데이트
            Cart cart = existingCart.get();
//            cart.setQty();
        } else {
            // 없으면 새로 추가
            Cart cart = Cart.builder()
                    .product(product)
                    .qty(cartListDTO.getQty())
                    .member(member)
                    .build();
            cartRepository.save(cart);
        }

    }

    // 장바구니 목록 조회
    public List<CartListDTO> list(String email) {

        List<CartListDTO> cartItems = cartRepository.findCartItemsByMemberEmail(email);

        return cartItems.stream()
                .map(cart -> CartListDTO.builder()
                        .email(cart.getEmail())
                        .bno(cart.getBno())
                        .pno(cart.getPno())
                        .pname(cart.getPname())
                        .price(cart.getPrice())
                        .qty(cart.getQty())
                        .build())
                .collect(Collectors.toList());

    }

    // 상품 개별 삭제
    public void softDeleteByProduct(String email, Long pno) {
        Cart cart = cartRepository.findByMemberEmailAndProductPno(email, pno)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cart.softDelete(); // 엔티티의 softDelete 메서드 호출
    }

    // 상품 수량 변경
    public void changeQty(Long pno, int qty) {
        // 장바구니 항목 조회
        Cart cart = cartRepository.findById(pno)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // 수량 변경
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than zero.");
        }

        cart.setQty(qty); // 새로운 수량 설정
        cartRepository.save(cart); // 변경 저장
    }

    // 장바구니 전체 비우기
    public void softDeleteAll(String email) {
        cartRepository.softDeleteAllByEmail(email);
    }


}
