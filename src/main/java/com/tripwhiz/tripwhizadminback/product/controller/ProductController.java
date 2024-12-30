package com.tripwhiz.tripwhizadminback.product.controller;

import com.tripwhiz.tripwhizadminback.product.entity.ThemeCategory;
import com.tripwhiz.tripwhizadminback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizadminback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizadminback.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${com.tripwhiz.uploadBasic}")
    private String uploadPath;

    @Value("${com.tripwhiz.upload.productpath}")
    private String productPath;

    // 이미지 조회
    @GetMapping("/image/{fileName}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        String imagePath = uploadPath + File.separator + productPath + File.separator + fileName;

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

        // 파일 MIME 타입 결정
        String mimeType = Files.probeContentType(imageFile.toPath());

        return ResponseEntity.ok()
                .header("Content-Type", mimeType)
                .body(imageBytes);
    }

    // 상품 목록 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductListDTO>> list(
            @RequestParam(required = false) List<Long> tnos,
            @RequestParam(required = false) Long cno,
            @RequestParam(required = false) Long scno) {

        log.info("상품 목록 요청: tnos={}, cno={}, scno={}", tnos, cno, scno);
        List<ProductListDTO> response = productService.searchProducts(tnos, cno, scno);
        return ResponseEntity.ok(response);
    }

    // 상품 검색
//    @GetMapping("/list/search")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<PageResponseDTO<ProductListDTO>> searchWithFilters(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) Integer minPrice,
//            @RequestParam(required = false) Integer maxPrice,
//            @RequestParam(required = false) Long tno,
//            @RequestParam(required = false) Long cno,
//            @RequestParam(required = false) Long scno,
//            PageRequestDTO pageRequestDTO) {
//        log.info("상품 키워드 검색 요청 - keyword: {}, minPrice: {}, maxPrice: {}, tno: {}, cno: {}, scno: {}",
//                keyword, minPrice, maxPrice, tno, cno, scno);
//
//        PageResponseDTO<ProductListDTO> result = productService.searchWithFilters(keyword, minPrice, maxPrice, tno, cno, scno, pageRequestDTO);
//        return ResponseEntity.ok(result);
//    }


    // 특정 상품 ID로 조회 (Native Query 사용)
    @GetMapping("/read/native/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductReadDTO> getProductNative(@PathVariable Long pno) {
        log.info("Attempting to fetch product with ID: {} using Native Query", pno);
        try {
            Optional<ProductReadDTO> productObj = productService.getProductByIdNative(pno);
            return productObj.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("No product found with ID: {} using Native Query", pno);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error occurred while fetching product with ID: {} using Native Query", pno, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createProduct(
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles


    ) throws IOException {
        log.info("Raw JSON received: {}", productListDTOJson);

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO;

        try {
            log.info("productListDTO JSON 변환 시작");
            productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
            log.debug("productListDTO 변환 성공: {}", productListDTO);

            log.info("Received ProductListDTO: {}", productListDTO);
            log.info("Received tnos: {}", productListDTO.getTnos());

        } catch (JsonProcessingException e) {
            log.error("JSON 변환 오류 - 입력 데이터: {}", productListDTOJson, e);
            throw e;
        }

        log.info("Parsed ProductListDTO: {}", productListDTO);

        Long createdProductId = productService.createProduct(productListDTO, imageFiles);
        return ResponseEntity.ok(createdProductId);
    }



    @PutMapping("/update/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateProduct(
            @PathVariable Long pno,
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws IOException {

        log.info("Received pno: {}", pno);

        // pno 검증
        if (pno == null || pno <= 0) {
            log.error("Invalid Product ID (pno): {}", pno);
            return ResponseEntity.badRequest().build();
        }

        // JSON 문자열을 객체로 변환
        ProductListDTO productListDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing productListDTO JSON: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // imageFiles가 없는 경우 처리
        if (imageFiles == null || imageFiles.isEmpty()) {
            log.warn("No image files provided for update. Proceeding without images.");
            imageFiles = Collections.emptyList();
        }

        // 상품 업데이트
        Long updatedProductPno;
        try {
            updatedProductPno = productService.updateProduct(pno, productListDTO, imageFiles);
        } catch (Exception e) {
            log.error("Error updating product with pno {}: {}", pno, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 수정된 상품 ID 반환
        log.info("Product updated successfully with ID: {}", updatedProductPno);
        return ResponseEntity.ok(updatedProductPno);
    }






    // 상품 삭제
    @PutMapping("/delete/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long pno) {
        productService.deleteProduct(pno);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/themes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ThemeCategory>> getThemes() {
        log.info("테마 목록 조회 요청 수신");
        List<ThemeCategory> themes = productService.getThemes();
        log.info("테마 목록 조회 성공 - 개수: {}", themes.size());
        return ResponseEntity.ok(themes);
    }

}
