package com.example.demo.cart.service;

import com.example.demo.cart.domain.Cart;
import com.example.demo.cart.dto.CartListDTO;
import com.example.demo.cart.dto.CartProductDTO;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.member.domain.Member;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.repository.ProductRepository;
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
//    private final ProductRepository productRepository;
//    private final OrderRepository orderRepository;


    // 장바구니에 물건 추가
    public void addToCart(CartProductDTO cartProductDTO) {

        Product product = Product.builder().pno(cartProductDTO.getPno()).build();
        Member member = Member.builder().email(cartProductDTO.getEmail()).build();

        // 장바구니에서 해당 제품 찾기
        Optional<Cart> existingCart = cartRepository.findByProduct(cartProductDTO.getPno());

        if (existingCart.isPresent()) {
            // 기존 제품이 있으면 수량 업데이트
            Cart cart = existingCart.get();
            cart.changeQty(cartProductDTO.getQty());
        } else {
            // 없으면 새로 추가
            Cart cart = Cart.builder()
                    .product(product)
                    .qty(cartProductDTO.getQty())
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
                        .qty(cart.getQty())
                        .build())
                .collect(Collectors.toList());

    }

    public void softDeleteByProduct(String email, Long pno) {
        Cart cart = cartRepository.findByMemberEmailAndProductPno(email, pno)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cart.softDelete(); // 엔티티의 softDelete 메서드 호출
    }

    public void softDeleteAll(String email) {
        cartRepository.softDeleteAllByEmail(email);
    }


//    public void saveCart(String email) {
//        // 장바구니 목록 조회
//        List<CartListDTO> cartList = list(email);
//
//        log.info("Received cart list for saving to order table: {}", cartList);
//
//        // cartList 데이터를 Order 테이블에 저장
//        for (CartListDTO cart : cartList) {
//            Order order = Order.builder()
////                    .email(cart.getEmail())
//                    .productNo(cart.getPno())
//                    .quantity(cart.getQty())
//                    .orderDate(LocalDateTime.now())
//                    .status("CREATED") // 상태 값은 필요에 따라 변경 가능
//                    .build();
//
//            orderRepository.save(order); // Order 엔티티 저장
//        }
//
//        log.info("Cart list successfully saved to order table.");
//    }



}
