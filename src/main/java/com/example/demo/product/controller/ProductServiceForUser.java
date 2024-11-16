package com.example.demo.product.controller;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.product.domain.Product;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceForUser {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final RestTemplate restTemplate;

    // application.yml 파일에서 User API URL을 불러와 변수에 저장
    @Value("${com.example.user.api.url}")
    private String userApiUrl;


    /**
     * 상품을 생성하고 User API로 데이터를 전송하는 메서드
     * @param productListDTO 상품 데이터 DTO
     * @return 생성된 상품의 ID
     */
    public Long createProduct(ProductListDTO productListDTO) {
        // Category와 SubCategory를 찾아서 상품 엔티티에 전달
        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        // ProductListDTO에서 엔티티로 변환한 후 데이터베이스에 저장
        Product product = productListDTO.toEntity(category, subCategory);
        Product savedProduct = productRepository.save(product);

        // 생성된 상품 정보를 User API로 전송
        sendProductToUserApi(productListDTO);

        log.info("Product created with ID: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

    /**
     * User API에 상품 정보를 전송하는 메서드
     * @param productListDTO 전송할 상품 데이터 DTO
     */
    private void sendProductToUserApi(ProductListDTO productListDTO) {
        try {
            // HTTP 헤더에 Content-Type을 JSON으로 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // HttpEntity를 생성하여 요청 본문과 헤더를 함께 설정
            HttpEntity<ProductListDTO> request = new HttpEntity<>(productListDTO, headers);
            String userApiEndpoint = userApiUrl + "/api/product/add";  // User API의 상품 추가 엔드포인트

            // User API로 POST 요청을 보내고 응답을 받음
            ResponseEntity<Long> response = restTemplate.postForEntity(userApiEndpoint, request, Long.class);

            // 요청이 성공했는지 여부를 확인하여 로그 출력
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Product successfully sent to User API, ID: {}", response.getBody());
            } else {
                log.error("Failed to send product to User API: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 발생 시 오류 로그 출력
            log.error("Error sending product to User API", e);
        }
    }
}
