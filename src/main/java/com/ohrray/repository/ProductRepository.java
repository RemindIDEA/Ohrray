package com.ohrray.repository;

import com.ohrray.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p , pi "+
        " from Product p join p.productImg pi " +
        " order by p.id desc")
    List<Product> getListWithImg(Pageable pageable);

    @Query("select p, pi " +
            "from Product p " +
            "join p.productImg pi " +
            "where p.id = :id")
    Product getOneProduct (@Param("id")Long id);

    @Query("select p , pi "+
            " from Product p join p.productImg pi "+
            " where p.productName like %:keyword%"
    )
    Page<Object[]> getListPage(Pageable pageable ,@Param("keyword")String keyword);

    @Query("select p , pi "+
            " from Product p join p.productImg pi "
    )
    Page<Object[]> getListPage(Pageable pageable);
}
