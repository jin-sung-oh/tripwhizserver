//package com.example.demo.product.repository;
//
//import com.example.demo.category.domain.CategoryProduct;
//import com.example.demo.product.domain.Product;
//import com.example.demo.product.dto.ProductReadDTO;
//import com.example.demo.product.repository.search.ProductSearch;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//
//public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
//
//    // 특정 상품을 ProductReadDTO 형태로 조회
//    @Query("select " +
//            "new com.example.demo.product.dto.ProductReadDTO(p.pno, p.pname, p.pdesc, p.price, p.images) " +
//            "from Product p " +
//            "left join p.images i " +
//            "where p.pno = :pno")
//    Optional<ProductReadDTO> read(@Param("pno") Long pno);
//
//    // 특정 상품의 CategoryProduct 정보를 조회
//    @Query("select cp from CategoryProduct cp " +
//            "join fetch cp.product p " +
//            "join fetch cp.category c " +
//            "where p.pno = :pno")
//    Optional<CategoryProduct> findCategory(@Param("pno") Long pno);
//
//    // 상위 카테고리를 기준으로 상품 목록 조회 (페이징 처리)
//    @Query("select p from Product p " +
//            "join p.category c " +
//            "where c.cno = :cno")
//    Page<Product> findByCategoryCno(@Param("cno") Long cno, Pageable pageable);
//
//    // 하위 카테고리를 기준으로 상품 목록 조회 (페이징 처리)
//    @Query("select p from Product p " +
//            "join p.subCategory sc " +
//            "where sc.scno = :scno")
//    Page<Product> findBySubCategoryScno(@Param("scno") Long scno, Pageable pageable);
//
//    // 테마 카테고리를 기준으로 상품 목록 조회 (페이징 처리)
//    @Query("select p from Product p " +
//            "where p.themeCategory = :themeCategory")
//    Page<Product> findByThemeCategory(@Param("themeCategory") String themeCategory, Pageable pageable);
//}
