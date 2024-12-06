package com.example.demo.product.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.domain.ThemeCategory;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/product")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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


    // 특정 상품 ID로 조회 (이미지 포함)
    @GetMapping("/read/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);

        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        return productObj.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("해당 ID의 상품을 찾을 수 없습니다: {}", pno);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createProduct(
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestPart("themeCategoryId") Long themeCategoryId) throws JsonProcessingException, IOException {

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);

        log.info("Received product create request: {}", productListDTO);
        log.info("Received themeCategoryId: {}", themeCategoryId);

        // 서비스 호출
        Long createdProductPno = productService.createProduct(productListDTO, imageFiles, themeCategoryId);

        // 생성된 상품 ID 반환
        return ResponseEntity.ok(createdProductPno);
        }



    // 상품 수정
    @PutMapping("/update/{pno}/{themeCategoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateProduct(
            @PathVariable Long pno,
            @PathVariable Long themeCategoryId, // themeCategoryId를 PathVariable로 받기
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
        Long updatedProductPno = productService.updateProduct(pno, productListDTO, imageFiles, themeCategoryId);

        // 수정된 상품 ID 반환
        return ResponseEntity.ok(updatedProductPno);
    }



    // 상품 삭제
    @PutMapping("/delete/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long pno) {

        log.info("Received product deletion request for PNO {}", pno);
        productService.deleteProduct(pno);
        return ResponseEntity.ok().build();

    }

}
