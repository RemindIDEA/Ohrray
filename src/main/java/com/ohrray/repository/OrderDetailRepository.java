package com.ohrray.repository;

import com.ohrray.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query("select o from OrderDetail  o where o.orders.id = :orderId")
    List<OrderDetail> findByOrderId(Long orderId);


}
