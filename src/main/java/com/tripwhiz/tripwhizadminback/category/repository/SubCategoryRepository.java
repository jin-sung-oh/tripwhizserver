package com.tripwhiz.tripwhizadminback.category.repository;


import com.tripwhiz.tripwhizadminback.category.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategory_Cno(Long cno);

}
