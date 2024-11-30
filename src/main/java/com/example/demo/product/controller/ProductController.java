package com.example.demo.product.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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


    // 특정 상품 ID로 조회 (이미지 포함)
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("ID로 상품을 조회합니다: {}", pno);

        Optional<ProductReadDTO> productObj = productService.getProductById(pno);

        return productObj.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("해당 ID의 상품을 찾을 수 없습니다: {}", pno);
                    return ResponseEntity.notFound().build();
                });
    }

    // 상품 생성
    @PostMapping("/add")
    public ResponseEntity<Long> createProduct(@RequestBody ProductListDTO productListDTO) {
        log.info("Received product creation request: {}", productListDTO);
        Long productId = productService.createProduct(productListDTO);
        return ResponseEntity.ok(productId);
    }

    // 상품 수정
    @PutMapping("/update/{pno}")
    public ResponseEntity<Long> updateProduct(@PathVariable Long pno, @RequestBody ProductListDTO productListDTO) {
        log.info("Received product update request for PNO {}: {}", pno, productListDTO);
        Long updatedProductPno = productService.updateProduct(pno, productListDTO);
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
