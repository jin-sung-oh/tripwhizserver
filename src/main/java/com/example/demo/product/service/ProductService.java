package com.example.demo.product.service;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductTheme;
import com.example.demo.product.domain.ThemeCategory;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.ProductThemeRepository;
import com.example.demo.product.repository.ThemeCategoryRepository;
import com.example.demo.util.CustomFileUtil;
import com.example.demo.util.file.domain.AttachFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CustomFileUtil customFileUtil;
    private final RestTemplate restTemplate;
    private final ThemeCategoryRepository themeCategoryRepository;
    private final ProductThemeRepository productThemeRepository;

    @Value("${com.example.user.api.url}")
    private String userApiUrl;

    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);
        return productRepository.read(pno);
    }

    // 상품 필터링
    public PageResponseDTO<ProductListDTO> searchProducts(Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO) {
        log.info("상품 목록을 조회합니다: tno = {}, cno = {}, scno = {}", tno, cno, scno);
        return productRepository.findByFiltering(tno, cno, scno, pageRequestDTO);
    }

    public Long createProduct(ProductListDTO productListDTO, List<MultipartFile> imageFiles, Long themeCategoryId) throws IOException {
        log.info("상품 생성 요청 시작: {}", productListDTO);

        try {
            // 카테고리, 서브카테고리 조회
            Category category = categoryRepository.findById(productListDTO.getCno())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));

            SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                    .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

            // Product 엔티티로 변환
            Product product = productListDTO.toEntity(category, subCategory);
            log.info("Product 생성 준비 완료: {}", product);

            // 이미지 파일 업로드 처리
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile imageFile = imageFiles.get(i);
                String savedImageName = customFileUtil.uploadProductImageFile(imageFile);
                AttachFile attachFile = new AttachFile(i + 1, savedImageName);
                product.addAttachFile(attachFile);
            }

            // 상품 저장
            Product savedProduct = productRepository.save(product);
            log.info("Product 저장 완료: {}", savedProduct.getPno());

            // 테마 카테고리 조회 (themeCategoryId 사용)
            ThemeCategory themeCategory = themeCategoryRepository.findById(themeCategoryId)
                    .orElseThrow(() -> new RuntimeException("ThemeCategory not found with ID: " + themeCategoryId));

            // ProductTheme 생성
            ProductTheme productTheme = new ProductTheme();
            productTheme.setProduct(savedProduct);
            productTheme.setThemeCategory(themeCategory);

            // ProductTheme 저장
            productThemeRepository.save(productTheme);
            log.info("ProductTheme 저장 완료");

            // 외부 API로 상품 정보 전송
            sendProductToUserApi(productListDTO, imageFiles, "/api/admin/product/add", HttpMethod.POST, themeCategory);

            return savedProduct.getPno();
        } catch (Exception e) {
            log.error("상품 생성 중 오류 발생: {}", e.getMessage());
            throw e; // 예외를 다시 던져서 외부에서 처리할 수 있게 함
        }
    }


    public Long updateProduct(Long pno, ProductListDTO productListDTO, List<MultipartFile> imageFiles, Long themeCategoryId) throws IOException {
        log.info("상품 수정 요청 시작: pno = {}", pno);

        try {
            // 기존 상품 조회
            Product product = productRepository.findById(pno)
                    .orElseThrow(() -> {
                        log.error("Product 조회 실패: pno = {}", pno);
                        return new RuntimeException("Product not found with ID: " + pno);
                    });

            // 카테고리, 서브카테고리 조회
            Category category = categoryRepository.findById(productListDTO.getCno())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));

            SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                    .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

            // 기존 ProductTheme 조회
            ProductTheme productTheme = productThemeRepository.findByProduct(product)
                    .orElseThrow(() -> new RuntimeException("ProductTheme not found for productId: " + product.getPno()));

            // 테마 카테고리 조회 (themeCategoryId 사용)
            ThemeCategory themeCategory = themeCategoryRepository.findById(themeCategoryId)
                    .orElseThrow(() -> new RuntimeException("ThemeCategory not found with ID: " + themeCategoryId));

            // Product 업데이트
            product.updateFromDTO(productListDTO, category, subCategory);
            log.info("Product 업데이트 완료: {}", product);

            // 기존 이미지 파일 삭제하고 새 이미지 추가
            product.getAttachFiles().clear();
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile imageFile = imageFiles.get(i);
                String savedImageName = customFileUtil.uploadProductImageFile(imageFile);
                AttachFile attachFile = new AttachFile(i + 1, savedImageName);
                product.addAttachFile(attachFile);
            }

            // 수정된 상품 저장
            Product updatedProduct = productRepository.save(product);
            log.info("Product 수정 완료: {}", updatedProduct.getPno());

            // User API에 수정된 상품 정보 전송 (ThemeCategory 객체 전달)
            sendProductToUserApi(productListDTO, imageFiles, "/api/product/update/" + pno, HttpMethod.PUT, themeCategory);

            return updatedProduct.getPno();
        } catch (Exception e) {
            log.error("상품 수정 중 오류 발생", e);
            throw e;
        }
    }

    // 상품 삭제
    public void deleteProduct(Long pno) {
        log.info("상품 삭제 요청 시작: pno = {}", pno);

        try {
            Product product = productRepository.findById(pno)
                    .orElseThrow(() -> {
                        log.error("Product 조회 실패: pno = {}", pno);
                        return new RuntimeException("Product not found with ID: " + pno);
                    });

            product.changeDelFlag(true);
            productRepository.save(product);
            log.info("Product 소프트 삭제 완료: pno = {}", pno);

            sendProductDelete("/api/product/delete/" + pno, HttpMethod.PUT);
        } catch (Exception e) {
            log.error("상품 삭제 중 오류 발생", e);
            throw e;
        }
    }

    private void sendProductToUserApi(ProductListDTO productListDTO, List<MultipartFile> imageFiles, String endpoint, HttpMethod httpMethod, ThemeCategory themeCategory) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ObjectMapper objectMapper = new ObjectMapper();

            // ProductListDTO를 JSON으로 변환
            String jsonProduct = objectMapper.writeValueAsString(productListDTO);

            // tno 값을 별도로 JSON으로 추가 (themeCategory에서 tno만 추출)
            body.add("productListDTO", jsonProduct);
            body.add("themeCategoryId", themeCategory.getTno()); // themeCategory의 tno 값을 별도로 추가

            // 이미지 파일이 있을 경우 추가
            if (imageFiles != null) {
                for (MultipartFile file : imageFiles) {
                    body.add("imageFiles", new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    });
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            String userApiEndpoint = userApiUrl + endpoint;

            // API 호출
            ResponseEntity<Long> response = restTemplate.exchange(userApiEndpoint, httpMethod, request, Long.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("User API 전송 성공, ID: {}", response.getBody());
            } else {
                log.error("User API 전송 실패: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("User API 전송 중 오류 발생", e);
        }
    }

    // User API에 삭제 요청
    private void sendProductDelete(String endpoint, HttpMethod httpMethod) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Void> request = new HttpEntity<>(headers);
            String userApiEndpoint = userApiUrl + endpoint;

            ResponseEntity<Void> response = restTemplate.exchange(userApiEndpoint, httpMethod, request, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("User API 삭제 요청 성공");
            } else {
                log.error("User API 삭제 요청 실패: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("User API 삭제 요청 중 오류 발생", e);
        }
    }
}
