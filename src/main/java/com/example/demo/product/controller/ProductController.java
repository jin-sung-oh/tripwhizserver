package com.example.demo.product.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${com.example.uploadBasic}")
    private String uploadPath;

    @Value("${com.example.upload.productpath}")
    private String productPath;

    // 이미지 조회
    @GetMapping("/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        log.info("이미지 파일을 조회합니다: {}", fileName);

        // 동적으로 이미지 경로 생성
        String imagePath = uploadPath + File.separator + productPath + File.separator + fileName;

        // 이미지 파일 읽기
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            log.warn("이미지 파일이 존재하지 않습니다: {}", fileName);
            return ResponseEntity.notFound().build();
        }

        // 파일 데이터를 바이트 배열로 변환
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

        // 파일 MIME 타입 결정
        String mimeType = Files.probeContentType(imageFile.toPath());

        return ResponseEntity.ok()
                .header("Content-Type", mimeType) // MIME 타입 설정
                .body(imageBytes);
    }

    // 상품 목록 조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(
            @RequestParam(required = false) Long tno,
            @RequestParam(required = false) Long cno,
            @RequestParam(required = false) Long scno,
            @Validated PageRequestDTO pageRequestDTO) {
        log.info("상품 목록을 조회합니다. tno: {}, cno: {}, scno: {}", tno, cno, scno);

        PageResponseDTO<ProductListDTO> response = productService.searchProducts(tno, cno, scno, pageRequestDTO);

        log.info("상품 목록 응답: {}", response);
        return ResponseEntity.ok(response);
    }

    // 상품 키워드 검색 및 가격 필터링(JH)
    @GetMapping("/list/search")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> searchWithFilters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long tno,
            @RequestParam(required = false) Long cno,
            @RequestParam(required = false) Long scno,
            PageRequestDTO pageRequestDTO) {
        log.info("상품 키워드 검색 요청 - keyword: {}, minPrice: {}, maxPrice: {}", keyword, minPrice, maxPrice);

        PageResponseDTO<ProductListDTO> result = productService.searchWithFilters(keyword, minPrice, maxPrice, tno, cno, scno, pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 특정 상품 ID로 조회 (Native Query 사용)
    @GetMapping("/read/native/{pno}")
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

    // 상품 생성
    @PostMapping("/add")
    public ResponseEntity<Long> createProduct(
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws JsonProcessingException, IOException {

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);

        log.info("Received Product: {}", productListDTO);

        if (imageFiles != null) {
            log.info("Received {} image files", imageFiles.size());
        }

        // 서비스 호출
        Long productId = productService.createProduct(productListDTO, imageFiles);

        // 생성된 상품 ID 반환
        return ResponseEntity.ok(productId);
    }


    // 상품 수정
    @PutMapping("/update/{pno}")
    public ResponseEntity<Long> updateProduct(
            @PathVariable Long pno,
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws JsonProcessingException, IOException {

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);

        log.info("Received product update request for PNO {}: {}", pno, productListDTO);

        if (imageFiles != null) {
            log.info("Received {} image files", imageFiles.size());
        }

        // 서비스 호출
        Long updatedProductPno = productService.updateProduct(pno, productListDTO, imageFiles);

        // 수정된 상품 ID 반환
        return ResponseEntity.ok(updatedProductPno);
    }

    // 상품 삭제
    @PutMapping("/delete/{pno}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long pno) {

        log.info("Received product deletion request for PNO {}", pno);
        productService.deleteProduct(pno);
        return ResponseEntity.ok().build();

    }

}
