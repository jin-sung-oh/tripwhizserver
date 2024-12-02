//package com.example.demo.order.repostory;
//
//import com.example.demo.manager.repository.StoreOwnerRepository;
//import com.example.demo.order.domain.Order;
//import com.example.demo.order.repository.OrderRepository;
//import com.example.demo.product.domain.Product;
//import com.example.demo.product.repository.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@SpringBootTest
//public class OrderDummyDataTest {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StoreOwnerRepository storeOwnerRepository;
//
//    @Test
//    public void createDummyOrders() {
//        Random random = new Random();
//        List<Product> products = productRepository.findAll(); // 모든 제품 가져오기
//
//        if (products.isEmpty()) {
//            System.out.println("No products found in the database.");
//            return;
//        }
//
//        // 점주의 sno가 2와 3 중에서 랜덤으로 선택
//        List<Integer> storeOwnerIds = List.of(2, 3);
//
//        List<Order> dummyOrders = new ArrayList<>();
//
//        for (int i = 0; i < 100; i++) { // 100개의 더미 데이터 생성
//            int sno = storeOwnerIds.get(random.nextInt(storeOwnerIds.size())); // 2 또는 3 선택
//            Product product = products.get(random.nextInt(products.size())); // 임의의 상품 선택
//
//            Order order = Order.builder()
//                    .storeOwner(StoreOwner.builder().sno((long) sno).build()) // 점주 정보
//                    .product(product) // 상품 정보
//                    .quantity(random.nextInt(5) + 1) // 수량 1~5 랜덤
//                    .totalPrice(product.getPrice() * (random.nextInt(5) + 1)) // 총 가격 계산
//                    .build();
//
//            dummyOrders.add(order);
//        }
//
//        orderRepository.saveAll(dummyOrders); // 더미 데이터 저장
//        System.out.println("100개의 더미 주문이 생성되었습니다.");
//    }
//}
