package com.ohrray.repository;

import com.ohrray.entity.CommunityBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard,Long> {
    @Query(" select b from" +
            " CommunityBoard b" +
            " where b.community.id = :cid")
    Page<CommunityBoard> findAllCommunityBoard(@Param("cid") Long cid, Pageable pageable);

}
