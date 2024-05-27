package com.ohrray.repository;

import com.ohrray.entity.Option;
import com.ohrray.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {


    @Query("select o from Option o where o.product.id=:id ")
    public List<Option> findbyProduct_id( @Param("id") Long pno);

    @Query("select o from Option o where o.product = :product")
    public List<Option> findByProduct(@Param("product") Product product);

    @Query("SELECT o.size FROM Option o WHERE o.product.id = :productId")
    List<String> findSizesByProductId(@Param("productId") Long productId);

    @Query("SELECT o.color FROM Option o WHERE o.product.id = :productId")
    List<String> findColorsByProductId(@Param("productId") Long productId);
}
