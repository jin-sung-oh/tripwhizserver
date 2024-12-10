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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/admin/product")
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
        String imagePath = uploadPath + File.separator + productPath + File.separator + fileName;

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String mimeType = Files.probeContentType(imageFile.toPath());

        return ResponseEntity.ok()
                .header("Content-Type", mimeType)
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
        PageResponseDTO<ProductListDTO> response = productService.searchProducts(tno, cno, scno, pageRequestDTO);
        return ResponseEntity.ok(response);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createProduct(
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
        Long createdProductPno = productService.createProduct(productListDTO, imageFiles);
        return ResponseEntity.ok(createdProductPno);
    }

    @PutMapping("/update/{pno}/{themeCategoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateProduct(
            @PathVariable Long pno,
            @PathVariable Long themeCategoryId,
            @RequestPart("productListDTO") String productListDTOJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws JsonProcessingException, IOException {

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
        Long updatedProductPno = productService.updateProduct(pno, productListDTO, imageFiles, themeCategoryId);
        return ResponseEntity.ok(updatedProductPno);
    }

    // 상품 삭제
    @PutMapping("/delete/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long pno) {
        productService.deleteProduct(pno);
        return ResponseEntity.ok().build();

    }

}
