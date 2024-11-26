package com.example.demo.cart.repository;

import com.example.demo.cart.domain.Cart;
import com.example.demo.cart.dto.CartListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT new com.example.demo.cart.dto.CartListDTO(c.bno, c.member.mno, c.product.pno, c.totalQty, c.totalPrice) " +
            "FROM Cart c")
    List<CartListDTO> findAllCartItems();

}
