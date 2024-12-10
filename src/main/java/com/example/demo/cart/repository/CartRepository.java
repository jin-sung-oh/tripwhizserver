package com.example.demo.cart.repository;

import com.example.demo.cart.domain.Cart;
import com.example.demo.cart.dto.CartListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 멤버별 장바구니 리스트 조회
    @Query("SELECT new com.example.demo.cart.dto.CartListDTO(m.email, c.bno, p.pno, p.pname, p.price, c.qty, c.delFlag) " +
            "FROM Cart c " +
            "JOIN c.member m " +
            "JOIN c.product p " +
            "WHERE m.email = :email AND c.delFlag = false")
    List<CartListDTO> findCartItemsByMemberEmail(@Param("email") String email);

    // 특정 제품이 장바구니에 있는지 확인 (회원 고려 X)
    @Query("SELECT c FROM Cart c WHERE c.product.pno = :pno")
    Optional<Cart> findByProduct(Long pno);

    // 특정 제품이 장바구니에 있는지 확인 (회원 고려 O)
    @Query("SELECT c FROM Cart c WHERE c.member.email = :email AND c.product.pno = :pno")
    Optional<Cart> findByMemberEmailAndProductPno(@Param("email") String email, @Param("pno") Long pno);

    // 멤버별 장바구니 비우기
    @Modifying
    @Query("UPDATE Cart c SET c.delFlag = true WHERE c.member.email = :email")
    void softDeleteAllByEmail(@Param("email") String email);

    // 장바구니에 있는 모든 항목 조회
//    @Query("SELECT new com.tripwhiz.tripwhizuserback.cart.dto.CartProductDTO(" +
//            "c.product.pno, c.product.pname, c.product.price, c.qty) " +
//            "FROM Cart c")
//    List<CartProductDTO> findAllCartItems();

}
