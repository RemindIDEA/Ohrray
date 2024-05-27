package com.ohrray.repository;

import com.ohrray.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {
    @Query(" select r " +
            " from ProductReview r  " +
            " join fetch r.member m " +
            " join fetch r.product p" +
            " where r.product.id= :pno")
    Optional<List<ProductReview>> findByProduct(@Param("pno") Long pno);
}
