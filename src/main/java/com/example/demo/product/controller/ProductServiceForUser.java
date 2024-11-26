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
import org.springframework.http.HttpMethod;
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


    // 생성된 상품 데이터 전송
    public void sendProductToUserApi(ProductListDTO productListDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<ProductListDTO> request = new HttpEntity<>(productListDTO, headers);
            String endpoint = userApiUrl + "/api/product/add";

            restTemplate.postForEntity(endpoint, request, Long.class);

            log.info("Product successfully sent to User API.");
        } catch (Exception e) {
            log.error("Error sending product to User API", e);
        }
    }

    // 수정된 상품 데이터 전송
    public void sendProductUpdateToUserApi(Long pno, ProductListDTO productListDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<ProductListDTO> request = new HttpEntity<>(productListDTO, headers);
            String endpoint = userApiUrl + "/api/product/update/" + pno;

            restTemplate.exchange(endpoint, HttpMethod.PUT, request, Void.class);

            log.info("Product successfully updated on User API, ID: {}", pno);
        } catch (Exception e) {
            log.error("Error updating product on User API", e);
        }
    }

    // 삭제 요청 전송
    public void sendProductDeleteToUserApi(Long pno) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Void> request = new HttpEntity<>(headers);
            String endpoint = userApiUrl + "/api/product/delete/" + pno;

            restTemplate.exchange(endpoint, HttpMethod.DELETE, request, Void.class);

            log.info("Product successfully deleted on User API, ID: {}", pno);
        } catch (Exception e) {
            log.error("Error deleting product on User API", e);
        }
    }
}
