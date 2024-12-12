package com.example.demo.product.repository;

import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.ProductTheme;
import com.example.demo.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 기존 상품 조회 Native Query
    @Query(value = "SELECT p.pno AS pno, p.pname AS pname, p.pdesc AS pdesc, p.price AS price, " +
            "c.cno AS cno, c.cname AS cname, " +
            "sc.scno AS scno, sc.sname AS sname, " +
            "JSON_ARRAYAGG(JSON_OBJECT('ord', af.ord, 'file_name', af.file_name)) AS attachFiles " +
            "FROM product p " +
            "LEFT JOIN category c ON p.category_cno = c.cno " +
            "LEFT JOIN sub_category sc ON p.sub_category_scno = sc.scno " +
            "LEFT JOIN product_images af ON af.pno = p.pno " +
            "WHERE p.pno = :pno " +
            "GROUP BY p.pno, c.cno, c.cname, sc.scno, sc.sname", nativeQuery = true)
    Optional<Map<String, Object>> readNative(@Param("pno") Long pno);

    // 상품 조회 쿼리 수정 (tnos 포함)
    @Query(value = """
SELECT p.pno, 
       p.pname, 
       p.pdesc, 
       p.price, 
       p.category_cno, 
       p.sub_category_scno,
       JSON_ARRAYAGG(DISTINCT tc.tno) AS tnos, -- 테마 ID를 배열로 반환
       JSON_ARRAYAGG(JSON_OBJECT('ord', af.ord, 'file_name', af.file_name)) AS attachFiles -- 수정된 부분
FROM product p
LEFT JOIN product_theme pt ON p.pno = pt.product_pno
LEFT JOIN theme_category tc ON pt.theme_category_tno = tc.tno
LEFT JOIN product_images af ON af.pno = p.pno
WHERE (:tnos IS NULL OR tc.tno IN (:tnos))
  AND (:cno IS NULL OR p.category_cno = :cno)
  AND (:scno IS NULL OR p.sub_category_scno = :scno)
GROUP BY p.pno
""", nativeQuery = true)
    List<Map<String, Object>> findProductsWithThemesAndAttachments(
            @Param("tnos") List<Long> tnos,
            @Param("cno") Long cno,
            @Param("scno") Long scno
    );
}


