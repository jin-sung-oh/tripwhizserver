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

    // Native Query로 특정 상품을 Map 형태로 조회
    @Query(value = "SELECT p.pno AS pno, p.pname AS pname, p.pdesc AS pdesc, p.price AS price, " +
            "c.cno AS cno, sc.scno AS scno, " +
            "JSON_ARRAYAGG(JSON_OBJECT('ord', af.ord, 'filename', af.filename)) AS attachFiles " +
            "FROM product p " +
            "LEFT JOIN category c ON p.category_cno = c.cno " +
            "LEFT JOIN sub_category sc ON p.sub_category_scno = sc.scno " +
            "LEFT JOIN product_images af ON af.pno = p.pno " +
            "WHERE p.pno = :pno " +
            "GROUP BY p.pno", nativeQuery = true)
    Optional<Map<String, Object>> readNative(@Param("pno") Long pno);




        @Query("SELECT p FROM Product p " +
                "LEFT JOIN ProductTheme pt ON pt.product = p " +
                "LEFT JOIN ThemeCategory tc ON tc.tno = pt.themeCategory.tno " +
                "WHERE (:cno IS NULL OR p.category.cno = :cno) " +
                "AND (:scno IS NULL OR p.subCategory.scno = :scno) " +
                "AND (:tnos IS NULL OR tc.tno IN :tnos)")
        Page<Product> findByThemesIn(@Param("tnos") List<Long> tnos,
                                     @Param("cno") Long cno,
                                     @Param("scno") Long scno,
                                     Pageable pageable);

        @Query("SELECT p FROM Product p " +
                "WHERE (:cno IS NULL OR p.category.cno = :cno) " +
                "AND (:scno IS NULL OR p.subCategory.scno = :scno)")
        Page<Product> findAllWithFiltering(@Param("cno") Long cno,
                                           @Param("scno") Long scno,
                                           Pageable pageable);
    }

}


