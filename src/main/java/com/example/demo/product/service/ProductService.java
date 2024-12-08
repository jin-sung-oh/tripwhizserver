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
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    // application.yml 파일에서 User API URL을 불러와 변수에 저장
    @Value("${com.example.user.api.url}")
    private String userApiUrl;

    @Value("${com.example.upload.productpath}")
    private String productPath;

    public List<ThemeCategory> getThemes() {
        return themeCategoryRepository.findAll();
    }
    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductByIdNative(Long pno) {
        log.info("ID로 상품을 조회합니다 (Native Query): {}", pno);

        Optional<Map<String, Object>> nativeResult = productRepository.readNative(pno);
        log.info("Native Query 실행 결과: {}", nativeResult);

        if (nativeResult.isPresent()) {
            Map<String, Object> resultMap = nativeResult.get();
            log.info("Result Map: {}", resultMap);

            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열을 List<AttachFile>로 변환
            List<AttachFile> attachFiles = null;
            try {
                String attachFilesJson = (String) resultMap.get("attachFiles");
                if (attachFilesJson != null) {
                    attachFiles = objectMapper.readValue(attachFilesJson, new TypeReference<List<AttachFile>>() {});
                }
            } catch (Exception e) {
                log.error("attachFiles 변환 오류: ", e);
            }

            ProductReadDTO dto = new ProductReadDTO(
                    ((Number) resultMap.get("pno")).longValue(),
                    (String) resultMap.get("pname"),
                    (String) resultMap.get("pdesc"),
                    ((Number) resultMap.get("price")).intValue(),
                    ((Number) resultMap.get("cno")) != null ? ((Number) resultMap.get("cno")).longValue() : null,
                    ((Number) resultMap.get("scno")) != null ? ((Number) resultMap.get("scno")).longValue() : null,
                    attachFiles // 변환된 List<AttachFile> 전달
            );

            log.info("Mapped ProductReadDTO: {}", dto);
            return Optional.of(dto);
        }

        log.warn("Native Query 결과가 존재하지 않습니다.");
        return Optional.empty();
    }


    //상품 필터링
    public PageResponseDTO<ProductListDTO> searchProducts(Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO) {
        log.info("상품 목록을 조회합니다", tno, cno, scno);


        return productRepository.findByFiltering(tno, cno, scno, pageRequestDTO);
    }

    // 상품 생성
    public Long createProduct(ProductListDTO productListDTO, List<MultipartFile> imageFiles) throws IOException {
        log.info("Start: 상품 생성 요청 - 데이터: {}", productListDTO);

        // Category와 SubCategory를 조회
        log.info("카테고리 조회 - cno: {}", productListDTO.getCno());
        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", productListDTO.getCno());
                    return new RuntimeException("Category not found with ID: " + productListDTO.getCno());
                });

        log.info("서브 카테고리 조회 - scno: {}", productListDTO.getScno());
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> {
                    log.error("SubCategory not found with ID: {}", productListDTO.getScno());
                    return new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno());
                });

        // toEntity 호출 시 Category와 SubCategory 전달
        log.debug("Product 엔티티 생성 시작");
        Product product = productListDTO.toEntity(category, subCategory);
        log.debug("Product 엔티티 생성 완료 - 데이터: {}", product);

        // 이미지 파일 처리
        if (imageFiles != null && !imageFiles.isEmpty()) {
            log.info("이미지 파일 처리 시작 - 파일 개수: {}", imageFiles.size());
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile imageFile = imageFiles.get(i);
                String savedImageName = customFileUtil.uploadProductImageFile(imageFile);
                AttachFile attachFile = new AttachFile(i + 1, savedImageName);
                product.addAttachFile(attachFile);
                log.debug("첨부 파일 추가 - 파일명: {}, ord: {}", savedImageName, attachFile.getOrd());
            }
            log.info("이미지 파일 처리 완료");
        }

        // 상품 저장
        log.info("Product 저장 시작");
        Product savedProduct = productRepository.save(product);
        log.info("Product 저장 완료 - pno: {}", savedProduct.getPno());

        // 테마 카테고리 연결
        log.info("테마 카테고리 연결 시작 - tnos: {}", productListDTO.getTnos());
        List<ThemeCategory> themeCategories = themeCategoryRepository.findAllById(productListDTO.getTnos());
        for (ThemeCategory themeCategory : themeCategories) {
            ProductTheme productTheme = ProductTheme.builder()
                    .product(savedProduct)
                    .themeCategory(themeCategory)
                    .build();
            productThemeRepository.save(productTheme);
            log.debug("ProductTheme 저장 완료 - themeCategory: {}", themeCategory.getTno());
        }
        log.info("테마 카테고리 연결 완료");

        // User API 호출
        log.info("User API 전송 시작");
        sendProductToUserApi(productListDTO, imageFiles, "/api/product/add", HttpMethod.POST, null);
        log.info("User API 전송 완료");

        log.info("End: 상품 생성 완료 - pno: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

    public Long updateProduct(Long pno, ProductListDTO productListDTO, List<MultipartFile> imageFiles) throws IOException {
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));

        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

        product.updateFromDTO(productListDTO, category, subCategory);

        // 기존 AttachFile 삭제
        product.getAttachFiles().clear();

        // 새로운 이미지 파일 업로드 및 AttachFile 생성
        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);
            String savedImageName = customFileUtil.uploadProductImageFile(imageFile);

            // AttachFile 생성 (ord는 i + 1로 설정)
            AttachFile attachFile = new AttachFile(i + 1, savedImageName);
            product.addAttachFile(attachFile);
        }

        Product updatedProduct = productRepository.save(product);

        // User API로 상품 업데이트 요청 전송
        sendProductToUserApi(productListDTO, imageFiles, "/api/product/update/" + pno, HttpMethod.PUT, null);
        log.info("User API request sent for updating product - PNO: {}", updatedProduct.getPno());

        return updatedProduct.getPno();
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

    private void sendProductToUserApi(ProductListDTO productListDTO, List<MultipartFile> imageFiles, String endpoint, HttpMethod httpMethod, ThemeCategory themeCategory) {
        try {
            log.info("Starting User API Request");
            log.debug("Endpoint: {}", endpoint);
            log.debug("HTTP Method: {}", httpMethod);

            // MultiValueMap으로 요청 데이터 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(productListDTO);
            body.add("productListDTO", jsonProduct);
            log.debug("Serialized ProductListDTO: {}", jsonProduct);

            if (themeCategory != null) {
                body.add("themeCategoryId", themeCategory.getTno());
                log.debug("ThemeCategoryId: {}", themeCategory.getTno());
            }

            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile file : imageFiles) {
                    body.add("imageFiles", new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    });
                    log.debug("Attached File: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());
                }
            }

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            log.debug("Request Headers: {}", headers);

            // HttpEntity 생성
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            log.debug("HttpEntity Body: {}", body);

            // User API Endpoint 설정
            String userApiEndpoint = userApiUrl + endpoint;
            log.info("Sending request to User API: {}", userApiEndpoint);

            // API 호출
            ResponseEntity<Long> response = restTemplate.exchange(userApiEndpoint, httpMethod, request, Long.class);

            log.info("User API Response: Status - {}, Body - {}", response.getStatusCode(), response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("User API returned error: Status - {}, Body - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("User API 전송 중 오류 발생", e);
        } catch (Exception e) {
            log.error("Unexpected error during User API request", e);
            throw new RuntimeException("User API 전송 중 예기치 않은 오류 발생", e);
        }
    }


    // User API에 상품 정보를 전송하는 메서드(삭제)
    private void sendProductDelete(String endpoint, HttpMethod httpMethod) {
        try {
            // HTTP 헤더에 Content-Type을 JSON으로 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<Void> request = new HttpEntity<>(headers);
            String userApiEndpoint = userApiUrl + endpoint;

            restTemplate.exchange(userApiEndpoint, httpMethod, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("User API 삭제 요청 중 오류 발생", e);
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

