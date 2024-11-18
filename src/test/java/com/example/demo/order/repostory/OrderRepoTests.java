package com.example.demo.order.repostory;

import com.example.demo.member.domain.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.order.domain.Order;
import com.example.demo.order.domain.OrderDetails;
import com.example.demo.order.domain.OrderStatus;
import com.example.demo.order.repository.OrderDetailsRepository;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.product.domain.Product;
import com.example.demo.product.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepoTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Random random = new Random();

    // Dummy 데이터 삽입 테스트
    @Test
    @Commit
    @Transactional
    public void insertDummies() {
        for (int i = 1; i <= 10; i++) {

            // Member 저장
            Member member = memberRepository.save(
                    Member.builder()
                            .name("Member" + i)
                            .email("member" + i + "@example.com")
                            .pw("1111")
                            .build()
            );

            // Order 생성 및 저장
            Order order = Order.builder()
                    .member(member) // Member 연관
                    .totalAmount(5 * i) // 제품 갯수
                    .totalPrice(10000 * i) // 총 가격
                    .createtime(LocalDateTime.now())
                    .pickupdate(LocalDateTime.now().plusDays(i))
                    .status(OrderStatus.PREPARING)
                    .build();

            orderRepository.save(order);
        }

        log.info("Dummy orders inserted successfully.");
    }

    // 특정 Member의 주문 페이징 조회 테스트
    @Test
    public void orderTest() {
        Long mno = 1L; // 조회할 Member ID
        Pageable pageable = PageRequest.of(0, 5); // 첫 번째 페이지, 5개씩 조회

        Page<Order> result = orderRepository.findByMemberMno(mno, pageable);

        log.info("Orders found: " + result.getTotalElements());

        result.forEach(order -> log.info(order));

    }

    @Test
    @Commit
    public void createDummyOrderDetails() {
        // 주문(Order) 목록 조회
        List<Order> orders = orderRepository.findAll();

        // 예외 처리: 주문 데이터가 없는 경우
        if (orders.isEmpty()) {
            throw new IllegalStateException("Order data is missing. Please add initial data.");
        }

        // 더미 데이터 20개 생성
        for (int i = 0; i < 20; i++) {
            // 무작위로 Order 선택
            Order randomOrder = orders.get(random.nextInt(orders.size()));

            // Product를 pno 1~100 범위에서 무작위 선택
            long randomPno = random.nextInt(100) + 1; // 1부터 100까지의 무작위 값
            Optional<Product> optionalProduct = productRepository.findById(randomPno);

            // 해당 pno가 존재하지 않으면 스킵
            if (optionalProduct.isEmpty()) {
                i--; // 반복을 다시 시도
                continue;
            }

            Product randomProduct = optionalProduct.get();

            // 무작위 amount 생성 (1~10 사이)
            int randomAmount = random.nextInt(10) + 1;

            // OrderDetails 생성
            OrderDetails orderDetails = OrderDetails.builder()
                    .order(randomOrder)
                    .product(randomProduct)
                    .amount(randomAmount)
                    .build();

            // 저장
            orderDetailsRepository.save(orderDetails);
        }

        System.out.println("Dummy data for OrderDetails table created successfully!");
    }

}
