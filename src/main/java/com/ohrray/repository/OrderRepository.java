package com.ohrray.repository;

import com.ohrray.entity.Member;
import com.ohrray.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query(" select o from Orders o where o.member = :member")
    public List<Orders> findByMember(Member member);
}
