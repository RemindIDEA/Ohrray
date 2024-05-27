package com.ohrray.repository;

import com.ohrray.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.social = :social and m.email =:email")
    Optional<Member> findByEmailandSocial(String email, boolean social);

    @Query("select m from Member m where m.email =:email")
    Optional<Member> findByEmail(String email);

    @Query("delete from Member m where m.email = :email")
    void deleteByEmail(@Param("email") String email);
}
