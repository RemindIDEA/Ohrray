package com.ohrray.repository;

import com.ohrray.entity.CommunityReply;
import com.ohrray.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<CommunityReply,Long> {

    @Query("SELECT r FROM CommunityReply r JOIN FETCH" +
            " r.communityBoard b " +
            " Join fetch r.member m"+
            " WHERE b.id = :id")
    Optional<List<CommunityReply>> findByBoard(@Param("id") Long id);

    @Query("SELECT r FROM CommunityReply r " +
            "JOIN FETCH" +
            " r.communityBoard b " +
            " Join fetch " +
            "r.member m"+
            " WHERE r.id = :id")
    Optional<CommunityReply> findByIds(@Param("id")Long id);

}
