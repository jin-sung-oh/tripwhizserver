package com.example.demo.product.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${com.example.uploadBasic}")
    private String uploadPath;

    @Value("${com.example.upload.productpath}")
    private String productPath;

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

    @GetMapping("/read/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        Optional<ProductReadDTO> productObj = productService.getProductById(pno);
        return productObj.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
        ObjectMapper objectMapper = new ObjectMapper();
        ProductListDTO productListDTO = objectMapper.readValue(productListDTOJson, ProductListDTO.class);
        Long updatedProductPno = productService.updateProduct(pno, productListDTO, imageFiles, themeCategoryId);
        return ResponseEntity.ok(updatedProductPno);
    }

    @PutMapping("/delete/{pno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long pno) {
        productService.deleteProduct(pno);
        return ResponseEntity.ok().build();
    }
}
