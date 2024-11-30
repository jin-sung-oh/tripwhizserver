package com.example.demo.product.service;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.domain.Product;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.repository.ProductRepository;
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

import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final RestTemplate restTemplate;

    // application.yml 파일에서 User API URL을 불러와 변수에 저장
    @Value("${com.example.user.api.url}")
    private String userApiUrl;


    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);
        return productRepository.read(pno);
    }


    //상품 필터링
    public PageResponseDTO<ProductListDTO> searchProducts(Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO) {
        log.info("상품 목록을 조회합니다", tno, cno, scno);


        return productRepository.findByFiltering(tno, cno, scno, pageRequestDTO);
    }


    // 상품 생성
    public Long createProduct(ProductListDTO productListDTO) {
        // Category와 SubCategory를 조회
        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

        // toEntity 호출 시 Category와 SubCategory 전달
        Product product = productListDTO.toEntity(category, subCategory);
        Product savedProduct = productRepository.save(product);

        // 생성된 상품 정보를 User API로 전송
        sendProductToUserApi(productListDTO, "/api/product/add", HttpMethod.POST);

        log.info("Product created with ID: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

    // 상품 수정
    public Long updateProduct(Long pno, ProductListDTO productListDTO) {
        // Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        // Category와 SubCategory를 조회
        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

        // updateFromDTO 호출
        product.updateFromDTO(productListDTO, category, subCategory);

        productRepository.save(product);

        sendProductToUserApi(productListDTO, "/api/product/update/" + pno, HttpMethod.PUT);

        log.info("Product updated with ID: {}", pno);
        return pno;
    }

    // 상품 삭제
    public void deleteProduct(Long pno) {
        // Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        // 소프트 삭제 처리: delFlag를 true로 설정
        product.changeDelFlag(true);

        // 상품 수정 후 저장 (소프트 삭제 처리)
        productRepository.save(product);

        // 생성된 상품 정보를 User API로 전송 (상품 삭제 요청)
        sendProductDelete("/api/product/delete/" + pno, HttpMethod.PUT);

        log.info("Product soft-deleted with ID: {}", pno);
    }

    // User API에 상품 정보를 전송하는 메서드
    private void sendProductToUserApi(ProductListDTO productListDTO, String endpoint, HttpMethod httpMethod) {
        try {
            // HTTP 헤더에 Content-Type을 JSON으로 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // HttpEntity를 생성하여 요청 본문과 헤더를 함께 설정
            HttpEntity<ProductListDTO> request = new HttpEntity<>(productListDTO, headers);
            String userApiEndpoint = userApiUrl + endpoint;  // User API의 상품 추가 엔드포인트

            // User API로 POST 요청을 보내고 응답을 받음
            ResponseEntity<Long> response = restTemplate.exchange(userApiEndpoint, httpMethod, request, Long.class);


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

    // User API에 상품 정보를 전송하는 메서드(삭제)
    private void sendProductDelete(String endpoint, HttpMethod httpMethod) {
        try {
            // HTTP 헤더에 Content-Type을 JSON으로 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // HttpEntity 생성 (DELETE 요청이므로 본문은 비워둠)
            HttpEntity<Void> request = new HttpEntity<>(headers); // Empty body for DELETE request
            String userApiEndpoint = userApiUrl + endpoint;  // User API의 상품 삭제 엔드포인트

            // User API로 요청을 보내고 응답을 받음
            ResponseEntity<Void> response = restTemplate.exchange(userApiEndpoint, httpMethod, request, Void.class);

            // 요청이 성공했는지 여부를 확인하여 로그 출력
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Product soft-deleted successfully in User API.");
            } else {
                log.error("Failed to send product delete request to User API: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 발생 시 오류 로그 출력
            log.error("Error sending product delete request to User API", e);
        }
    }

}


// 상품 정보와 이미지를 함께 조회
//    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
//        log.info("ID로 상품 및 이미지를 조회합니다: {}", pno);
//
//        return productRepository.read(pno).map(product -> {
//            // 이미지 URL 처리
//            if (product.getFileName() != null) {
//                product.setFileName("/uploads/" + product.getFileName());
//                log.info("이미지 URL 처리 완료: {}", product.getFileName());
//            }
//            return product;
//        });
//    }


// Admin API에서 전송된 상품 정보를 DB에 저장하는 메서드
//    public void saveProductFromAdmin(ProductListDTO productListDTO) {
//        log.info("Saving product from admin: {}", productListDTO);
//
//        // Category와 SubCategory를 찾음
//        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
//                .orElseThrow(() -> new RuntimeException("Category not found for ID: " + productListDTO.getCategoryCno()));
//
//        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
//                .orElseThrow(() -> new RuntimeException("SubCategory not found for ID: " + productListDTO.getSubCategoryScno()));
//
//        // ProductListDTO를 Product 엔티티로 변환 및 저장
//        Product product = productListDTO.toEntity(category, subCategory);
//        productRepository.save(product);
//
//        log.info("Product saved successfully: {}", product);
//    }

