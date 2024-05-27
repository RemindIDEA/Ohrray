package com.ohrray.repository;

import com.ohrray.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory , Long> {

    @Query(" select c " +
            " from ProductCategory c " +
            " where c.subCategory = :subcate")
    public ProductCategory findBySubCategory(@Param("subcate")String subcategory);





}
