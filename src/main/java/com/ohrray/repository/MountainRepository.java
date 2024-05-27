package com.ohrray.repository;

import com.ohrray.entity.Mountain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MountainRepository extends JpaRepository<Mountain, Long> {
    @Query("select m from Mountain m where m.name like %:keyword%")
    Page<Mountain> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
