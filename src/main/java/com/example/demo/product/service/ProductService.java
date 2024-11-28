package com.example.demo.product.service;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.controller.ProductServiceForUser;
import com.example.demo.product.domain.Product;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import com.example.demo.util.file.domain.AttachFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductServiceForUser productServiceForUser;

    // 기본 상품 목록 조회
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list with pagination");
        return productRepository.listByCno(pageRequestDTO);
    }

//     상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);
        return productRepository.listByCategory(cno, pageRequestDTO);
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);
        return productRepository.listBySubCategory(scno, pageRequestDTO);
    }

    // 테마 카테고리로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByTheme(String themeCategory, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by theme category: {}", themeCategory);
        return productRepository.listByTheme(themeCategory, pageRequestDTO);
    }

//    // 상품 생성(SO)
//    public Long createProduct(ProductListDTO productListDTO, List<AttachFile> attachFiles) {
//        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
//                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
//
//        Product product = productListDTO.toEntity(category, subCategory);
//        if (attachFiles != null) {
//            attachFiles.forEach(product::addAttachFile);
//        }
//
//        Product savedProduct = productRepository.save(product);
//
//        // 유저 서버로 생성된 데이터 전송
//        productServiceForUser.sendProductToUserApi(productListDTO);
//
//        log.info("Product created with ID: {}", savedProduct.getPno());
//        return savedProduct.getPno();
//    }

    // 상품 생성(JH)
    public Long createProduct(ProductListDTO productListDTO, List<AttachFile> attachFiles) {
        log.info("ProductListDTO: {}", productListDTO);
        log.info("AttachFiles: {}", attachFiles);

        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Product product = productListDTO.toEntity(category, subCategory);

        if (attachFiles != null && !attachFiles.isEmpty()) {
            for (AttachFile attachFile : attachFiles) {
                log.info("Adding file: {}", attachFile); // 로그 추가
                product.addAttachFile(attachFile);
            }
        }

        Product savedProduct = productRepository.save(product);

        log.info("Product created with ID: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

//    // 상품 수정(SO)
//    public Long updateProduct(Long pno, ProductListDTO productListDTO, List<AttachFile> attachFiles) {
//        Product product = productRepository.findById(pno)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        product.updateFromDTO(productListDTO);
//        if (attachFiles != null) {
//            attachFiles.forEach(product::addAttachFile);
//        }
//
//        productRepository.save(product);
//
//        // 유저 서버로 수정된 데이터 전송
//        productServiceForUser.sendProductUpdateToUserApi(pno, productListDTO);
//
//        log.info("Product updated with ID: {}", pno);
//        return pno;
//    }

    //상품 업데이트(JH)
    public Long updateProduct(Long pno, ProductListDTO productListDTO, List<AttachFile> attachFiles) {
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 기존 데이터 업데이트
        product.updateFromDTO(productListDTO);

        // 기존 AttachFile 리스트를 초기화하고 새 파일 추가
        if (attachFiles != null && !attachFiles.isEmpty()) {
            for (AttachFile attachFile : attachFiles) {
                log.info("Adding file: {}", attachFile); // 로그 추가
                product.addAttachFile(attachFile);
            }
        }

        productRepository.save(product);

        log.info("Product updated with ID: {}", pno);
        return pno;
    }
// 상품삭제(SO)
//    public void deleteProduct(Long pno) {
//        productRepository.findById(pno)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        productRepository.deleteById(pno);
//
//        // 유저 서버로 삭제 요청 전송
//        productServiceForUser.sendProductDeleteToUserApi(pno);
//
//        log.info("Product deleted with ID: {}", pno);
//    }






    // 상품 삭제(JH)
//    public Long deleteProduct(Long pno) {
//        productRepository.findById(pno)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        productRepository.deleteById(pno);
//        log.info("Product deleted with ID: {}", pno);
//        return pno;
//    }
}