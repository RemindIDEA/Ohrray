package com.ohrray.repository;

import com.ohrray.entity.CommunityReply;
import com.ohrray.entity.ProductQnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductQnARepository extends JpaRepository<ProductQnA,Long> {
    @Query(" select r " +
            " from ProductQnA r  " +
            " where r.product.id= :pno")
    Optional<List<ProductQnA>> findByProduct(@Param("pno") Long pno);
}
