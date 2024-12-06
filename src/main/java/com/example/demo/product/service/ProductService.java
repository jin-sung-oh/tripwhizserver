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

    public Optional<ProductReadDTO> getProductById(Long pno) {
        return productRepository.read(pno);
    }

    public PageResponseDTO<ProductListDTO> searchProducts(Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO) {
        return productRepository.findByFiltering(tno, cno, scno, pageRequestDTO);
    }

    public Long createProduct(ProductListDTO productListDTO, List<MultipartFile> imageFiles) throws IOException {
        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));

        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

        Product product = productListDTO.toEntity(category, subCategory);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile imageFile = imageFiles.get(i);
                String savedImageName = customFileUtil.uploadProductImageFile(imageFile);
                AttachFile attachFile = new AttachFile(i + 1, savedImageName);
                product.addAttachFile(attachFile);
            }
        }

        Product savedProduct = productRepository.save(product);

        List<ThemeCategory> themeCategories = themeCategoryRepository.findAllById(productListDTO.getTnos());
        for (ThemeCategory themeCategory : themeCategories) {
            ProductTheme productTheme = ProductTheme.builder()
                    .product(savedProduct)
                    .themeCategory(themeCategory)
                    .build();
            productThemeRepository.save(productTheme);
        }

        sendProductToUserApi(productListDTO, imageFiles, "/api/product/add", HttpMethod.POST, null);

        return savedProduct.getPno();
    }

    public Long updateProduct(Long pno, ProductListDTO productListDTO, List<MultipartFile> imageFiles, Long themeCategoryId) throws IOException {
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        Category category = categoryRepository.findById(productListDTO.getCno())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productListDTO.getCno()));

        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + productListDTO.getScno()));

        ProductTheme productTheme = productThemeRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("ProductTheme not found for productId: " + product.getPno()));

        ThemeCategory themeCategory = themeCategoryRepository.findById(themeCategoryId)
                .orElseThrow(() -> new RuntimeException("ThemeCategory not found with ID: " + themeCategoryId));

        product.updateFromDTO(productListDTO, category, subCategory);

        product.getAttachFiles().clear();
        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);
            String savedImageName = customFileUtil.uploadProductImageFile(imageFile);
            AttachFile attachFile = new AttachFile(i + 1, savedImageName);
            product.addAttachFile(attachFile);
        }

        Product updatedProduct = productRepository.save(product);

        sendProductToUserApi(productListDTO, imageFiles, "/api/product/update/" + pno, HttpMethod.PUT, themeCategory);

        return updatedProduct.getPno();
    }

    public void deleteProduct(Long pno) {
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        product.changeDelFlag(true);
        productRepository.save(product);

        sendProductDelete("/api/product/delete/" + pno, HttpMethod.PUT);
    }

    private void sendProductToUserApi(ProductListDTO productListDTO, List<MultipartFile> imageFiles, String endpoint, HttpMethod httpMethod, ThemeCategory themeCategory) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonProduct = objectMapper.writeValueAsString(productListDTO);
            body.add("productListDTO", jsonProduct);

            if (themeCategory != null) {
                body.add("themeCategoryId", themeCategory.getTno());
            }

            if (imageFiles != null && !imageFiles.isEmpty()) {
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

            restTemplate.exchange(userApiEndpoint, httpMethod, request, Long.class);
        } catch (Exception e) {
            throw new RuntimeException("User API 전송 중 오류 발생", e);
        }
    }

    private void sendProductDelete(String endpoint, HttpMethod httpMethod) {
        try {
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
