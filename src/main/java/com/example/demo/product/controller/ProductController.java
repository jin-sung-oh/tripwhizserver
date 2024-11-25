package com.example.demo.product.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.service.ProductService;
import com.example.demo.util.file.domain.AttachFile;
import com.example.demo.util.file.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

//    상품 생성
//    @PostMapping("/add")
//    public ResponseEntity<Long> createProduct(
//        @RequestPart ProductListDTO productListDTO,
//        @RequestPart(required = false) MultipartFile file) {
//
//        Long pno = productService.createProduct(productListDTO);
//        return ResponseEntity.ok(pno);
//    }

    // uploadService 추가한 메서드(JH)
    @PostMapping("/add")
    public ResponseEntity<Long> createProduct(
            @RequestPart("productListDTO") ProductListDTO productListDTO,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {

        List<AttachFile> attachFiles = uploadService.uploadFiles(files); // 파일 업로드 처리
        Long pno = productService.createProduct(productListDTO, attachFiles); // 상품 생성
        return ResponseEntity.ok(pno);
    }

//    상품 수정
//    @PutMapping("/update/{pno}")
//    public ResponseEntity<Long> updateProduct(
//        @PathVariable Long pno,
//        @RequestPart ProductListDTO productListDTO,
//        @RequestPart(required = false) MultipartFile file) {
//
//        productService.updateProduct(pno, productListDTO);
//        return ResponseEntity.ok(pno);
//    }

    // uploadService 추가한 메서드(JH)
    @PutMapping("/update/{pno}")
    public ResponseEntity<Long> updateProduct(
            @PathVariable Long pno,
            @RequestPart ProductListDTO productListDTO,
            @RequestPart(required = false) MultipartFile[] files) {

        List<AttachFile> attachFiles = uploadService.uploadFiles(files); // 파일 업로드 처리
        productService.updateProduct(pno, productListDTO, attachFiles); // 상품 수정
        return ResponseEntity.ok(pno);
    }

    // 상품 삭제
    @DeleteMapping("/delete/{pno}")
    public ResponseEntity<Long> deleteProduct(@PathVariable Long pno) {
        productService.deleteProduct(pno);  // deleteBoard 대신 deleteProduct로 수정
        return ResponseEntity.ok(pno);
    }

    // 기본 상품 목록 조회 엔드포인트
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(@Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list");

        PageResponseDTO<ProductListDTO> response = productService.list(requestDTO);
        log.info("Product list response: {}", response);

        return ResponseEntity.ok(response);
    }

    // 특정 상품 ID로 조회 엔드포인트
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("Fetching product with ID: {}", pno);

        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        return productObj.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 상위 카테고리(cno)로 상품 목록 조회 엔드포인트
    @GetMapping("/list/category")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByCategory(@RequestParam Long cno, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);

        // 요청된 카테고리 ID를 PageRequestDTO에 설정
        requestDTO.setCategoryCno(cno);  // 카테고리 ID 설정 추가
        PageResponseDTO<ProductListDTO> response = productService.listByCategory(cno, requestDTO);
        log.info("Product list by category response: {}", response);

        return ResponseEntity.ok(response);
    }

    // 하위 카테고리(scno)로 상품 목록 조회 엔드포인트
    @GetMapping("/list/subcategory")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listBySubCategory(@RequestParam Long scno, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);

        PageResponseDTO<ProductListDTO> response = productService.listBySubCategory(scno, requestDTO);
        log.info("Product list by sub-category response: {}", response);

        return ResponseEntity.ok(response);
    }

    // 테마 카테고리로 상품 목록 조회 엔드포인트
    @GetMapping("/list/theme")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByTheme(@RequestParam String themeCategory, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by theme category: {}", themeCategory);

        PageResponseDTO<ProductListDTO> response = productService.listByTheme(themeCategory, requestDTO);
        log.info("Product list by theme response: {}", response);

        return ResponseEntity.ok(response);
    }

}