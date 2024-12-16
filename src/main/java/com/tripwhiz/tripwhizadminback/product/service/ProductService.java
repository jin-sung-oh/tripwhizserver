package com.tripwhiz.tripwhizadminback.product.service;

import com.tripwhiz.tripwhizadminback.category.entity.Category;
import com.tripwhiz.tripwhizadminback.category.entity.SubCategory;
import com.tripwhiz.tripwhizadminback.category.repository.CategoryRepository;
import com.tripwhiz.tripwhizadminback.category.repository.SubCategoryRepository;
import com.tripwhiz.tripwhizadminback.product.entity.Product;
import com.tripwhiz.tripwhizadminback.product.entity.ThemeCategory;
import com.tripwhiz.tripwhizadminback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizadminback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizadminback.product.repository.ProductRepository;
import com.tripwhiz.tripwhizadminback.product.repository.ThemeCategoryRepository;
import com.tripwhiz.tripwhizadminback.util.CustomFileUtil;
import com.tripwhiz.tripwhizadminback.util.file.entity.AttachFile;
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
import java.util.*;

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
    @Value("${com.tripwhiz.user.api.url}")
    private String userApiUrl;

    @Value("${com.tripwhiz.upload.productpath}")
    private String productPath;


    public List<ThemeCategory> getThemes() {
        log.info("ThemeCategory 목록 조회 중...");
        List<ThemeCategory> themes = themeCategoryRepository.findAll();
        log.info("ThemeCategory 조회 완료: {}개 항목", themes.size());
        return themes;
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
                    attachFiles = objectMapper.readValue(attachFilesJson, new TypeReference<List<AttachFile>>() {
                    });
                }
            } catch (Exception e) {
                log.error("attachFiles 변환 오류: ", e);
            }

            // cname과 sname 로깅 추가
            String cname = (String) resultMap.get("cname");
            String sname = (String) resultMap.get("sname");

            log.info("Category Name (cname): {}", cname);
            log.info("SubCategory Name (sname): {}", sname);
            ProductReadDTO dto = new ProductReadDTO(
                    ((Number) resultMap.get("pno")).longValue(),
                    (String) resultMap.get("pname"),
                    (String) resultMap.get("pdesc"),
                    ((Number) resultMap.get("price")).intValue(),
                    ((Number) resultMap.get("cno")) != null ? ((Number) resultMap.get("cno")).longValue() : null,
                    ((Number) resultMap.get("scno")) != null ? ((Number) resultMap.get("scno")).longValue() : null,
                    attachFiles // 변환된 List<AttachFile>
            );

            log.info("Mapped ProductReadDTO with Category and SubCategory: {}", dto);


            log.info("Mapped ProductReadDTO: {}", dto);
            return Optional.of(dto);
        }

        log.warn("Native Query 결과가 존재하지 않습니다.");
        return Optional.empty();
    }


    public List<ProductListDTO> searchProducts(List<Long> tnos, Long cno, Long scno) {
        log.info("상품 목록을 조회합니다. tnos: {}, cno: {}, scno: {}", tnos, cno, scno);

        if (tnos != null && tnos.isEmpty()) {
            tnos = null;
        }

        List<Map<String, Object>> queryResults = productRepository.findProductsWithThemesAndAttachments(tnos, cno, scno);

        Map<Long, ProductListDTO> productMap = new HashMap<>();
        for (Map<String, Object> row : queryResults) {
            Long pno = ((Number) row.get("pno")).longValue();

            ProductListDTO dto = productMap.computeIfAbsent(pno, k -> ProductListDTO.builder()
                    .pno(pno)
                    .pname((String) row.get("pname"))
                    .pdesc((String) row.get("pdesc"))
                    .price(((Number) row.get("price")).intValue())
                    .cno(row.get("category_cno") != null ? ((Number) row.get("category_cno")).longValue() : null)
                    .scno(row.get("sub_category_scno") != null ? ((Number) row.get("sub_category_scno")).longValue() : null)
                    .tnos(new ArrayList<>())
                    .attachFiles(new ArrayList<>())
                    .build());

            // 카테고리 및 서브카테고리 추가
            Long categoryCno = ((Number) row.get("category_cno")).longValue();
            Long subCategoryScno = ((Number) row.get("sub_category_scno")).longValue();

            Category category = categoryRepository.findById(categoryCno).orElse(null);
            SubCategory subCategory = subCategoryRepository.findById(subCategoryScno).orElse(null);

            if (category != null) {
                dto.setCategory(category); // Category 객체 매핑
            }
            if (subCategory != null) {
                dto.setSubCategory(subCategory); // SubCategory 객체 매핑
            }

            // `tnos` 추가 (JSON으로 반환된 테마 카테고리 값 변환)
            String tnosJson = (String) row.get("tnos");
            if (tnosJson != null && !tnosJson.equals("[]")) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<Long> themeIds = objectMapper.readValue(tnosJson, new TypeReference<List<Long>>() {});
                    dto.getTnos().addAll(themeIds);
                } catch (Exception e) {
                    log.error("Tnos 변환 오류: ", e);
                }
            }

            // `attachFiles` 추가
            String attachFilesJson = (String) row.get("attachFiles");
            if (attachFilesJson != null && !attachFilesJson.equals("[]")) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<AttachFile> attachFiles = objectMapper.readValue(attachFilesJson, new TypeReference<List<AttachFile>>() {});
                    dto.getAttachFiles().addAll(attachFiles);
                } catch (Exception e) {
                    log.error("AttachFile 변환 오류: ", e);
                }
            }
        }

        return new ArrayList<>(productMap.values());
    }


//     상품 검색
//    public PageResponseDTO<ProductListDTO> searchWithFilters(
//            String keyword, Integer minPrice, Integer maxPrice,
//            Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO) {
//        log.info("상품 키워드 검색 및 필터링 실행 - keyword: {}, minPrice: {}, maxPrice: {}, tno: {}, cno: {}, scno: {}",
//                keyword, minPrice, maxPrice, tno, cno, scno);
//
//        return productRepository.searchWithKeywordAndFilters(keyword, minPrice, maxPrice, tno, cno, scno, pageRequestDTO);
//    }


    // 상품 생성
    public Long createProduct(ProductListDTO productListDTO, List<MultipartFile> imageFiles) throws IOException {
        log.info("Start creating product with ProductListDTO: {}", productListDTO);

        if (productListDTO.getTnos() == null || productListDTO.getTnos().isEmpty()) {
            throw new IllegalArgumentException("At least one theme category (tno) must be specified.");
        }

        // Validate cno and scno
        if (productListDTO.getCno() == null) {
            log.error("Category ID (cno) is null!");
            throw new IllegalArgumentException("Category ID (cno) must not be null");
        }
        if (productListDTO.getScno() == null) {
            log.error("SubCategory ID (scno) is null!");
            throw new IllegalArgumentException("SubCategory ID (scno) must not be null");
        }

        // Fetch Category
        log.debug("Fetching Category with ID: {}", productListDTO.getCno());
        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));

        // Fetch SubCategory
        log.debug("Fetching SubCategory with ID: {}", productListDTO.getScno());
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

        // toEntity 호출 시 Category와 SubCategory 전달
        Product product = productListDTO.toEntity(category, subCategory);

        // 파일 업로드 및 첨부 파일 추가
        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);
            String savedImageName = customFileUtil.uploadProductImageFile(imageFile);

            // AttachFile 생성 (ord는 i + 1로 설정)
            AttachFile attachFile = new AttachFile(i + 1, savedImageName);
            product.addAttachFile(attachFile);
        }

        //Product 저장
        Product savedProduct = productRepository.save(product);

        // 테마 정보 저장 (product_theme 테이블)
        if (productListDTO.getTnos() != null && !productListDTO.getTnos().isEmpty()) {
            for (Long tno : productListDTO.getTnos()) {
                log.debug("Fetching ThemeCategory with ID: {}", tno);
                ThemeCategory themeCategory = themeCategoryRepository.findById(tno)
                        .orElseThrow(() -> new RuntimeException("ThemeCategory not found with ID: " + tno));

                ProductTheme productTheme = new ProductTheme(savedProduct, themeCategory);
                productThemeRepository.save(productTheme);
            }
        }

        // 생성된 상품 정보를 User API로 전송
        sendProductToUserApi(productListDTO, imageFiles, "/api/product/add", HttpMethod.POST);

        log.info("Product created with ID: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

    public Long updateProduct(Long pno, ProductListDTO productListDTO, List<MultipartFile> imageFiles) throws IOException {
        log.info("Updating product with pno: {}", pno); // pno 확인
        log.info("Received ProductListDTO: {}", productListDTO); // DTO 확인

        // Category와 SubCategory 필드 확인
        log.info("Category ID (cno): {}", productListDTO.getCno());
        log.info("SubCategory ID (scno): {}", productListDTO.getScno());

        if (pno == null || pno <= 0) {
            throw new IllegalArgumentException("Product ID (pno) must be a valid non-null value");
        }

        // Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));
        log.info("Fetched product: {}", product);

        // Category와 SubCategory를 조회
        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));
        log.info("Fetched category: {}", category);

        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));
        log.info("Fetched subCategory: {}", subCategory);

        // updateFromDTO 호출
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

        // 상품 정보 업데이트
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated with ID: {}", updatedProduct.getPno());

        // 생성된 상품 정보를 User API로 전송
        sendProductToUserApi(productListDTO, imageFiles, "/api/product/update/" +pno, HttpMethod.POST);


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

    // User API에 상품 정보를 전송하는 메서드
    private void sendProductToUserApi(ProductListDTO productListDTO, List<MultipartFile> imageFiles, String endpoint, HttpMethod httpMethod) {
        try {
            // MultiValueMap으로 요청 데이터 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonProduct = objectMapper.writeValueAsString(productListDTO);

            // 직렬화된 JSON 데이터 로그
            log.debug("Serialized ProductListDTO JSON: {}", jsonProduct);

            body.add("productListDTO", jsonProduct);

            // 파일 데이터를 추가
            if (imageFiles != null) {
                for (MultipartFile file : imageFiles) {
                    body.add("imageFiles", new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    });
                    log.debug("Attached File: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());
                }
            } else {
                log.debug("No image files to attach.");
            }

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 인증 헤더가 필요한 경우 추가
            headers.set("Authorization", "Bearer " + "your_access_token"); // Replace with actual token
            log.debug("Request Headers: {}", headers);

            // HttpEntity 생성
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            log.debug("HttpEntity Body: {}", body);

            // User API Endpoint 설정
            String userApiEndpoint = userApiUrl + endpoint;
            log.info("Sending request to User API: {}", userApiEndpoint);

            // API 호출
            ResponseEntity<Long> response = restTemplate.exchange(userApiEndpoint, httpMethod, request, Long.class);

            // 요청 성공 여부 확인
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Product successfully sent to User API, Response Body: {}, Status Code: {}",
                        response.getBody(), response.getStatusCode());
            } else {
                log.error("Failed to send product to User API: {}, Response Body: {}",
                        response.getStatusCode(), response.getBody());
            }
        } catch (HttpClientErrorException e) {
            // HTTP 4xx 에러 로그
            log.error("HTTP Error while sending product to User API: {}, Response Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            // HTTP 5xx 에러 로그
            log.error("Server Error while sending product to User API: {}, Response Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // 기타 예외 로그
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

